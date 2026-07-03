package org.spring.ai.aihub.slopGenerator.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NewTweets {
    List<TweetVariants> tweets;
}
