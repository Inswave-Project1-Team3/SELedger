package util;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public abstract class Timestamped {

    private LocalDateTime localDateTime;
}
