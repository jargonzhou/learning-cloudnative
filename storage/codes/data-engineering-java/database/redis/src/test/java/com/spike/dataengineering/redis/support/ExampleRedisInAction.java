package com.spike.dataengineering.redis.support;

import org.redisson.api.*;
import org.redisson.client.protocol.ScoredEntry;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ExampleRedisInAction {

    public static class Article {
        public Long id; // 被设置
        public String title;
        public String link;
        public String poster;
        public Date time;
        public int votes; // 被设置

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Article [id=");
            builder.append(id);
            builder.append(", title=");
            builder.append(title);
            builder.append(", link=");
            builder.append(link);
            builder.append(", poster=");
            builder.append(poster);
            builder.append(", time=");
            builder.append(time);
            builder.append(", votes=");
            builder.append(votes);
            builder.append("]");
            return builder.toString();
        }
    }

    public void postArticle(RedissonClient redissonClient, Article article) {
        RAtomicLong ral = redissonClient.getAtomicLong("articleId");
        article.id = ral.addAndGet(1L);

        // article:
        String articleKey = "article:" + article.id;
        RMap<String, Object> articleMap = redissonClient.<String, Object>getMap(articleKey);
        articleMap.put("title", article.title);
        articleMap.put("link", article.link);
        articleMap.put("poster", article.poster);
        articleMap.put("time", article.time.getTime());
        articleMap.put("votes", 1); // default: vote 1

        // vote:
        String voteKey = "vote:" + article.id;
        RSet<String> voteSet = redissonClient.<String>getSet(voteKey);
        voteSet.add("user:" + article.poster);
        voteSet.expire(Duration.ofDays(7));

        // score:
        RScoredSortedSet<String> scoreZSet = redissonClient.<String>getScoredSortedSet("score:");
        scoreZSet.add(article.time.getTime() + 432, "article:" + article.id);

        // time:
        RScoredSortedSet<String> timeZSet =
                redissonClient.<String>getScoredSortedSet("time:" + article.id);
        timeZSet.add(article.time.getTime(), "article:" + article.id);
    }

    public void voteArticle(RedissonClient redissonClient, String userId, Long articleId) {
        RSet<String> voteSet = redissonClient.<String>getSet("vote:" + articleId);
        if (!voteSet.contains("user:" + userId)) {
            voteSet.add("user:" + userId);
        }

        RScoredSortedSet<String> scoreZSet = redissonClient.<String>getScoredSortedSet("score:");
        scoreZSet.addAndGetRank(432, "article:" + articleId);

        RMap<String, Object> articleMap =
                redissonClient.<String, Object>getMap("article:" + articleId);
        int votes = (int) articleMap.get("votes");
        articleMap.put("votes", votes + 1);
    }

    final int ARTICLE_PAGE_SIZE = 25;

    public List<Article> queryArticle(RedissonClient redissonClient, int page) {
        List<Article> result = Lists.newArrayList();

        int start = (page - 1) * ARTICLE_PAGE_SIZE;
        int end = start + ARTICLE_PAGE_SIZE - 1;

        RScoredSortedSet<String> scoreZSet = redissonClient.<String>getScoredSortedSet("score:");
        Collection<ScoredEntry<String>> articleIds = scoreZSet.entryRange(start, end);
        for (ScoredEntry<String> articleId : articleIds) {
            RMap<String, Object> articleMap =
                    redissonClient.<String, Object>getMap(articleId.getValue());
            Article article = new Article();
            article.id = Long.valueOf(articleId.getValue().substring("article:".length()));
            article.title = (String) articleMap.get("title");
            article.link = (String) articleMap.get("link");
            article.poster = (String) articleMap.get("poster");
            article.time = new Date((Long) articleMap.get("time"));
            article.votes = (int) articleMap.get("votes");
            result.add(article);
        }

        return result;
    }
}
