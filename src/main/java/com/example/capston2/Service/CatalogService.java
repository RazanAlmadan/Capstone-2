package com.example.capston2.Service;

import com.example.capston2.AI.AiService;
import com.example.capston2.Model.Catalog;
import com.example.capston2.Model.Designer;
import com.example.capston2.Repository.CatalogRepository;
import com.example.capston2.Repository.DesignerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final CatalogRepository catalogRepository;
    private final DesignerRepository designerRepository;
    private final AiService aiService;

    public List<Catalog> getAllCatalogs(){
        return catalogRepository.findAll();
    }

    public void addCatalog(Catalog catalog) throws Exception {

        // Only analyze if images exist
        if (catalog.getImages() != null && !catalog.getImages().isEmpty()) {

            String imageUrl = catalog.getImages().get(0);

            // Download image
            byte[] bytes = aiService.downloadImage(imageUrl);

            // Call AI once
            String style = aiService.analyzeImage(bytes);

            // Save style in DB
            catalog.setAiStyle(style);
        }

        catalogRepository.save(catalog);
        Designer designer = designerRepository.findDesignerById(catalog.getDesignerId());

        if (designer.getCatalogs() == null) {
            designer.setCatalogs(new ArrayList<>());
        }

        designer.getCatalogs().add(catalog.getId());
        designerRepository.save(designer);
        //return catalogRepository.save(catalog);
    }


    public Boolean updateCatalog(Integer id, Catalog catalog){
        Catalog oldCatalog = catalogRepository.findCatalogById(id);
        if (oldCatalog == null){
            return false;
        }
        oldCatalog.setDesignerId(catalog.getDesignerId());
        oldCatalog.setName(catalog.getName());
        oldCatalog.setImages(catalog.getImages());
        catalogRepository.save(oldCatalog);
        return true;
    }

    public Boolean deleteCatalog(Integer id){
        Catalog oldCatalog = catalogRepository.findCatalogById(id);
        if (oldCatalog == null){
            return false;
        }
        catalogRepository.delete(oldCatalog);
        return true;
    }


}