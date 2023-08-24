package example.Advanced.Music.app.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadProgress {
    private long uploadedBytes;
    private long totalBytes;
    private double progress;

    public UploadProgress(long uploadedBytes, long totalBytes, double progress) {
        this.uploadedBytes = uploadedBytes;
        this.totalBytes = totalBytes;
        this.progress = progress;
    }
}
