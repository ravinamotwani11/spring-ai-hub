package org.spring.ai.aihub.foodMenu.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Menu {
    String restaurantName;
    String sourceLanguage;
    String targetLanguage;
    List<MenuItem> menuItems;
}
