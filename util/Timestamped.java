package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Timestamped {
    private String createdAt;
    private String updatedAt;


    public Timestamped() {
        this.createdAt = LocalDateTime.now().withNano(0)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updatedAt = LocalDateTime.now().withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ;
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now().withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ;
    }
}
