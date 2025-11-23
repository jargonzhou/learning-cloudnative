package com.spike.security.service;

import com.spike.security.model.Document;
import com.spike.security.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    //    @PostAuthorize("hasPermission(returnObject, 'ROLE_admin'") // 方法层后置授权: 权限
    @PostAuthorize("hasPermission(#code, 'document', 'ROLE_admin')") // 使用ID
    public Document getDocument(String code) {
        return documentRepository.findDocument(code);
    }
}
