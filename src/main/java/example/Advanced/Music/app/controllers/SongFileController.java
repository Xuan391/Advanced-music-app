package example.Advanced.Music.app.controllers;

import example.Advanced.Music.app.services.SongStorageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("/api/v1")
public class SongFileController {
    @Autowired
    private SongStorageService songStorageService;

    @ApiOperation(value = "Api đọc file nhạc")
    @GetMapping("/musicFiles/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailSongFile(@PathVariable String fileName) {
        try {
            byte[] bytes = songStorageService.readFileContent(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(bytes);
        }
        catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }
}
