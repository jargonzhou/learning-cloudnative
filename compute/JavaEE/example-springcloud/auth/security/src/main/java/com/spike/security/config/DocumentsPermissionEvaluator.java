package com.spike.security.config;

import com.spike.security.model.Document;
import com.spike.security.repository.DocumentRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class DocumentsPermissionEvaluator implements PermissionEvaluator {
    private final DocumentRepository documentRepository;

    public DocumentsPermissionEvaluator(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {
        Document document = (Document) targetDomainObject;
        String p = (String) permission;

        boolean hasAuthority = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(p));

        // 有权限, 或者是文档属主
        return hasAuthority ||
                document.getOwner().equals(authentication.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        String code = targetId.toString(); // 文档ID
        Document document = documentRepository.findDocument(code);
        String p = (String) permission;

        boolean hasAuthority = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(p));

        // 有权限, 或者是文档属主
        return hasAuthority ||
                document.getOwner().equals(authentication.getName());
    }
}
