## WannaEat – AI-Powered Recipe Generator

### 🚀 Overview
WannaEat is a mobile app that generates random, tailored recipes based on user prompts using AI. It helps users quickly discover what to cook based on a starting idea.

### 🎯 Features
- Smart recipe generation via GPT-4o
- Ability to request a different list of recipes based on the same prompt
- Save and toggle favorites locally
- Fully offline access to saved recipes

### ⚙️ Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM + Repository Pattern
- **Libraries:** Retrofit, Gson, Coroutines, Jetpack Compose
- **AI Integration:** OpenAI API with function calling

### 🧠 Challenges
Parsing function-call JSON responses from OpenAI and mapping them into Kotlin models reliably was non-trivial and required a custom schema-based approach.

### 📸 Video walkthrough / Links
- https://we.tl/t-H8IvQ2jvnN   (please ping me if the link has expired)

### 🔮 Future Plans
- Image placement for each recipe (a result from another request or changing the existing schema to return a proper URL)
- Searchbar history
- Filters for the favorites screen (breakfast, dinner, vegan etc.)
- Meal plan based on a set of needs
- Unit test and UI tests
- Dark mode
- More animations to better engage the user 
