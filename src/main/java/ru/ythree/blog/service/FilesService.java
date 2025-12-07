package ru.ythree.blog.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ythree.blog.model.Image;
import ru.ythree.blog.repository.ImageRepository;

import java.io.IOException;
import java.util.Optional;

@Service
public class FilesService {
    private final ImageRepository imageRepository;

    public FilesService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void upload(MultipartFile file, Long postId) {
        String imageName = file.getOriginalFilename();
        Optional<Image> image = imageRepository.findByPostId(postId);
        try {
            if (image.isEmpty())
                imageRepository.save(imageName, file.getBytes(), postId);
            else
                imageRepository.update(imageName, file.getBytes(), postId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource download(Long postId) {
        Optional<Image> image = imageRepository.findByPostId(postId);
        return new ByteArrayResource(image.isEmpty() ? new byte[0] : image.get().getData());
    }
}
