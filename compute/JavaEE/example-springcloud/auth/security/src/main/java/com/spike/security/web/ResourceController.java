package com.spike.security.web;

import com.spike.security.model.Document;
import com.spike.security.model.Employee;
import com.spike.security.model.Product;
import com.spike.security.service.BookService;
import com.spike.security.service.DocumentService;
import com.spike.security.service.NameService;
import com.spike.security.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ResourceController {
    @Autowired
    private NameService nameService;
    @Autowired
    private BookService bookService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ProductService productService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //
    // 方法层授权
    //
    @GetMapping("/method/hello")
    public String helloMethod() {
        return "Hello, " + nameService.getName();
    }

    @GetMapping("/method/secret/names/{name}")
    public List<String> names(@PathVariable String name) {
        return nameService.getSecretNames(name);
    }

    @GetMapping("/method/book/details/{name}")
    public Employee getDetails(@PathVariable String name) {
        return bookService.getBookDetails(name);
    }

    // 方法权限
    @GetMapping("/method/documents/{code}")
    public Document getDocumentDetails(@PathVariable String code) {
        return documentService.getDocument(code);
    }

    //
    // 方法层过滤
    //
    @GetMapping("/method/filter/sell")
    public List<Product> sellProduct() {
        List<Product> products = new ArrayList<>(); // must be mutable
        products.add(new Product("beer", "nikolai"));
        products.add(new Product("candy", "nikolai"));
        products.add(new Product("chocolate", "julien"));
        return productService.sellProducts(products);
    }

    @GetMapping("/method/filter/find")
    public List<Product> findProduct() {
        return productService.findProducts();
    }
}
