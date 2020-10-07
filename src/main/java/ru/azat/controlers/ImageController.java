package ru.azat.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.azat.services.ImageService;
import ru.azat.transfer.ImageDto;

@RestController
@RequestMapping("/image")
@PreAuthorize("isAuthenticated()")
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

    @PostMapping("/generateRandom")
    public void generateRandom(@RequestParam("count") Integer count) {
        imageService.generateRandom(count);
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

    @PostMapping()
    public ResponseEntity createImage(@RequestBody ImageDto imageDto) {
        try {
            ImageDto image = imageService.createImage(imageDto);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
