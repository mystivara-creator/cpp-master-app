package com.example.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.CppCodeComment
import com.example.ui.theme.CppCodeKeyword
import com.example.ui.theme.CppCodeNumber
import com.example.ui.theme.CppCodeString
import com.example.ui.theme.CppCodeType
import com.example.ui.theme.TextPrimary
import java.util.regex.Pattern

object CppSyntaxHighlighter {

    private val KEYWORDS = setOf(
        "if", "else", "switch", "case", "default", "for", "while", "do",
        "break", "continue", "return", "class", "struct", "enum", "public",
        "private", "protected", "virtual", "override", "template", "typename",
        "namespace", "using", "friend", "delete", "new", "try", "catch", "throw",
        "const", "constexpr", "static", "auto", "nullptr", "true", "false", "sizeof"
    )

    private val TYPES = setOf(
        "int", "double", "float", "char", "bool", "void", "long", "short",
        "unsigned", "std::string", "std::vector", "std::map", "std::set",
        "std::unique_ptr", "std::shared_ptr", "std::weak_ptr", "std::cout",
        "std::cin", "std::endl", "string", "vector", "map", "set", "size_t"
    )

    // Pre-compile regular expressions once to avoid runtime lag
    private val COMMENT_PATTERN = Pattern.compile("(//[^\n]*|/\\*[\\s\\S]*?\\*/)")
    private val PREP_PATTERN = Pattern.compile("#\\w+(\\s*<[^>]+>|\\s*\"[^\"]+\")?")
    private val STRING_PATTERN = Pattern.compile("\"(\\\\.|[^\"])*\"")
    private val CHAR_PATTERN = Pattern.compile("'(\\\\.|[^'])*'")
    private val NUM_PATTERN = Pattern.compile("\\b\\d+(\\.\\d+)?f?\\b")
    private val WORD_PATTERN = Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*(::[a-zA-Z_][a-zA-Z0-9_]*)?\\b")

    fun highlightCppCode(code: String): AnnotatedString {
        if (code.isEmpty()) return AnnotatedString("")

        return buildAnnotatedString {
            append(code)

            // Base style
            addStyle(
                SpanStyle(
                    color = TextPrimary,
                    fontFamily = FontFamily.Monospace
                ),
                0,
                code.length
            )

            // 1. Comments
            val commentMatcher = COMMENT_PATTERN.matcher(code)
            while (commentMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeComment),
                    commentMatcher.start(),
                    commentMatcher.end()
                )
            }

            // 2. Preprocessor
            val prepMatcher = PREP_PATTERN.matcher(code)
            while (prepMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeKeyword, fontWeight = FontWeight.Bold),
                    prepMatcher.start(),
                    prepMatcher.end()
                )
            }

            // 3. String literals
            val stringMatcher = STRING_PATTERN.matcher(code)
            while (stringMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeString),
                    stringMatcher.start(),
                    stringMatcher.end()
                )
            }

            // 4. Character literals
            val charMatcher = CHAR_PATTERN.matcher(code)
            while (charMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeString),
                    charMatcher.start(),
                    charMatcher.end()
                )
            }

            // 5. Numbers
            val numMatcher = NUM_PATTERN.matcher(code)
            while (numMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeNumber),
                    numMatcher.start(),
                    numMatcher.end()
                )
            }

            // 6. Keywords & Types
            val wordMatcher = WORD_PATTERN.matcher(code)
            while (wordMatcher.find()) {
                val word = wordMatcher.group()
                when {
                    KEYWORDS.contains(word) -> {
                        addStyle(
                            SpanStyle(color = CppCodeKeyword, fontWeight = FontWeight.SemiBold),
                            wordMatcher.start(),
                            wordMatcher.end()
                        )
                    }
                    TYPES.contains(word) -> {
                        addStyle(
                            SpanStyle(color = CppCodeType, fontWeight = FontWeight.Medium),
                            wordMatcher.start(),
                            wordMatcher.end()
                        )
                    }
                }
            }
        }
    }
}
