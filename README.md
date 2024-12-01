# Online-Word-Lookup-Tool
Online API based word lookup application which takes words as input and returns its definition.

**Project Description:**
This software enables users to search for English words, providing quick results using a Trie data structure for lookup efficiency. The project also offers spelling correction and auto-suggestions through the edit distance algorithm. Instead of web scraping, it utilizes the Wordnik API to fetch word data.

**Key Features:**
**Efficient Word Lookup:**
Implements a Trie data structure for storing and searching words efficiently.
Offers near-instantaneous lookup performance.

**Spelling Correction & Auto-Suggestions:**
Uses the edit distance algorithm (Levenshtein distance) to suggest corrections for misspelled words.
Auto-suggestions are generated for partial inputs.

**Online Data Fetching:**
Utilizes the Wordnik API to fetch word definitions, synonyms, antonyms, and example usages dynamically.
Avoids the need for maintaining a large local dataset.

**Search History:**
Optionally stores recent searches for quick access.

**Interactive User Interface (CLI/GUI):**
Can be built as a Command Line Interface (CLI) for simplicity or a Graphical User Interface (GUI) using JavaFX/Swing.

**Technology Stack:**
Programming Language: Java
Data Structure: Trie for fast lookup and edit distance for correction.
API Integration: Wordnik API for word data.
IDE: IntelliJ IDEA/Eclipse.
