# 🌌 AI World Hub

An interactive **AI-powered multi-world application** built using **Spring Boot + Spring AI**, combining entertainment, wellness, food intelligence, and social media creativity into one beautiful animated AI experience.

The application provides multiple AI assistants, each with its own dedicated UI, theme, and functionality.

---

# 🚀 Features

## 🧛 Vampire Diaries AI

A conversational AI assistant inspired by the world of Mystic Falls.

### Features:

* Chat with a Vampire Diaries themed AI assistant
* Ask questions about characters, stories, and events
* Maintains conversation history
* Separate AI personality and theme

---
## 🍩 Simpsons AI

A fun Springfield-inspired AI assistant.

### Features:

* Chat with Simpsons-style AI
* Explore jokes, memories, and fictional conversations
* Persistent chat history
* Dedicated Simpsons visual theme

---

## 💪 Fitness Coach AI

A personal wellness assistant powered by AI.

### Features:

* Workout guidance
* Nutrition suggestions
* Lifestyle recommendations
* AI-powered fitness conversations

---

# 🍽 Smart Menu AI

An intelligent restaurant menu translator and audio assistant.

Upload any restaurant menu image and AI will analyze, translate, and organize it.

## Features:

### 📷 Menu Image Processing

* Upload menu images in any language
* AI extracts menu information
* Supports different restaurant menu formats

### 🌎 Translation

* Converts menus into English
* Preserves:

    * Dish names
    * Prices
    * Menu structure

### 📋 Smart Menu Formatting

Example output:

```
Starters

French Fries ............ 10$
Spring Rolls ............ 8$


Main Course

Chicken Curry ........... 28$
Mutton Curry ............ 31$


Desserts

Ice Cream ............... 8$
```

### 🔊 AI Voice Menu

Users can listen to the translated menu using text-to-speech.

Features:

* Natural audio generation
* Restaurant-style reading experience
* Easy menu accessibility

---

# 🐦 Tweet Creator AI

An AI-powered tweet transformation assistant.

The feature allows users to convert simple ideas into engaging modern tweets using AI.

---

## Features

### ✨ AI Tweet Generation

Users can provide:

* Original tweet idea
* Topic hint
* Emoji preference
* Modernization style

The AI generates multiple tweet variations.

Example:

Input:

```
How to learn programming fast
```

Output:

```
Unlock the full potential of Spring AI!
Believe in yourself and keep learning 🚀
```

---

## 🎨 Customization Controls

### Emoji Level

Users can choose:

* Low
* Medium
* High

Controls how many emojis are added to generated tweets.

---

### Modernization Level

Users can choose:

* Low
* Medium
* Extreme

Controls how much the AI transforms the original tweet.

Example:

Low:

```
Learn programming with dedication.
```

Extreme:

```
🚀 Level up your coding journey! Master the digital universe with AI-powered skills 🔥
```

---

# 📜 Tweet History

Tweet Creator AI stores previous submissions using browser local storage.

Features:

* Saves submitted tweets
* Maintains generated variants
* Allows users to revisit previous requests
* Clicking history shows all generated tweet versions

No database required.

---

# 🎨 UI Features

The application includes:

## Animated Landing Page

* AI world cards
* Interactive hover effects
* Colorful themes
* Smooth transitions

---

## Individual AI Themes

Each AI world has a dedicated appearance.

| AI World         | Theme                       |
| ---------------- | --------------------------- |
| Vampire Diaries  | Dark vampire red theme      |
| Simpsons         | Yellow Springfield theme    |
| Fitness          | Green wellness theme        |
| Smart Menu AI    | Restaurant theme            |
| Tweet Creator AI | Colorful social media theme |

---

# ✨ Animations

The application includes:

* Hover animations
* AI loading indicators
* Animated backgrounds
* Tweet generation spinner
* Firework effects
* Smooth page transitions

---

# 🏗️ Technology Stack

## Backend

* Java
* Spring Boot
* Spring AI
* REST APIs
* AI model integration

---

## Frontend

* HTML5
* CSS3
* JavaScript
* Local Storage
* GSAP animations

---

## AI Capabilities

Used for:

* Natural language conversation
* Menu translation
* Text transformation
* Tweet generation
* Audio script generation

---

# 📂 Project Structure

```
AI-World-Hub

├── src/main/java
│
│   ├── controllers
│   ├── services
│   └── AI integrations
│
├── src/main/resources
│
│   ├── static
│   │
│   │   ├── index.html
│   │   │
│   │   ├── css
│   │   │   ├── common.css
│   │   │   └── chat.css
│   │   │
│   │   └── js
│   │       ├── app-shell.js
│   │       └── ai-chat.js
│
└── README.md
```

---

# 🔌 API Endpoints

## Chat APIs

Example:

```
GET /tvd
GET /simpsons
GET /fitness
```

---

## Smart Menu APIs

Upload menu:

```
POST /menu/upload
```

Generate audio:

```
GET /menu/audio
```

---

## Tweet Creator API

Generate tweets:

```
GET /tweets
```

Example:

```
/tweets?
originalTweet=How to learn programming fast
&
topicHint=Spring AI
&
emojiLevel=high
&
modernizationLevel=extreme
```

Response:

```json
{
 "tweets":[
   {
    "tweet":"Unlock the future with Spring AI 🚀",
    "voice":null
   }
 ]
}
```

---

# 💾 Data Storage

## Browser Storage

Tweet history uses:

```
localStorage
```

Stored data includes:

* Original tweet
* Topic hint
* Emoji level
* Modernization level
* Generated tweet variations

---

# ▶️ Running the Application

## Backend

Run Spring Boot application:

```
mvn spring-boot:run
```

---

## Open Browser

```
http://localhost:8080
```

---

# 🌟 Future Enhancements

Possible improvements:

* User authentication
* Cloud-based history storage
* More AI worlds
* Image generation integration
* Voice conversation mode
* Mobile responsive application
* Social media direct publishing

---

# 👨‍💻 Author

Built with ❤️ using:

```
Spring Boot + Spring AI + Modern Web Technologies
```

A single platform bringing multiple AI experiences together.

Developed by **Ravina Motwani**
