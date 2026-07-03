package org.spring.ai.aihub.slopGenerator.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.ai.aiproject.slopGenerator.enums.Voice;

@Data
@NoArgsConstructor
public class TweetVariants {
    public String tweet;
    public Voice voice;
}
