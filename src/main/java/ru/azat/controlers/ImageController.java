package ru.azat.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.azat.services.ImageService;
import ru.azat.transfer.FileDto;
import ru.azat.transfer.ImageDto;

@RestController
@RequestMapping("/image")
@PreAuthorize("isAnonymous()")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/next")
    public ResponseEntity<ImageDto> getNextImage() {
        ImageDto imageDto = imageService.getNextImage();

        if (imageDto == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(imageDto);
        }
    }

    @GetMapping("/{imageId}")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<ImageDto> getImage(@PathVariable("imageId") Long imageId) {
        ImageDto imageDto = imageService.getImage(imageId);

        if (imageDto == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(imageDto);
        }
    }

    @GetMapping("/{imageId}/file")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<byte[]> getFile(@PathVariable("imageId") Long imageId) {
        FileDto file = imageService.getFile(imageId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @PutMapping("/{imageId}/status")
    public ResponseEntity updateStatus(
            @PathVariable("imageId") Long imageId,
            @RequestParam("rating") Float rating) {
        try {
            ImageDto image = imageService.setRating(imageId, rating);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity createImage(@RequestParam("file") MultipartFile file, ImageDto imageDto) {
        try {
            ImageDto image = imageService.createImage(file, imageDto);
            return ResponseEntity.status(HttpStatus.OK).body(image);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @PostMapping("/import")
    public ResponseEntity importFromZip(@RequestParam("file") MultipartFile file) {
        try {
            imageService.importZip(file);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
