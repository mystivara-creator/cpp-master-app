package com.example.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.CppCodeComment
import com.example.ui.theme.CppCodeFunction
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

    fun highlightCppCode(code: String): AnnotatedString {
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

            // 1. Comments: // ... or /* ... */
            val commentPattern = Pattern.compile("(//[^\n]*|/\\*[\\s\\S]*?\\*/)")
            val commentMatcher = commentPattern.matcher(code)
            while (commentMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeComment),
                    commentMatcher.start(),
                    commentMatcher.end()
                )
            }

            // 2. Preprocessor: #include <...> or #define
            val prepPattern = Pattern.compile("#\\w+(\\s*<[^>]+>|\\s*\"[^\"]+\")?")
            val prepMatcher = prepPattern.matcher(code)
            while (prepMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeKeyword, fontWeight = FontWeight.Bold),
                    prepMatcher.start(),
                    prepMatcher.end()
                )
            }

            // 3. String literals: "..."
            val stringPattern = Pattern.compile("\"(\\\\.|[^\"])*\"")
            val stringMatcher = stringPattern.matcher(code)
            while (stringMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeString),
                    stringMatcher.start(),
                    stringMatcher.end()
                )
            }

            // 4. Character literals: '.'
            val charPattern = Pattern.compile("'(\\\\.|[^'])*'")
            val charMatcher = charPattern.matcher(code)
            while (charMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeString),
                    charMatcher.start(),
                    charMatcher.end()
                )
            }

            // 5. Numbers
            val numPattern = Pattern.compile("\\b\\d+(\\.\\d+)?f?\\b")
            val numMatcher = numPattern.matcher(code)
            while (numMatcher.find()) {
                addStyle(
                    SpanStyle(color = CppCodeNumber),
                    numMatcher.start(),
                    numMatcher.end()
                )
            }

            // 6. Words: check keywords and types
            val wordPattern = Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*(::[a-zA-Z_][a-zA-Z0-9_]*)?\\b")
            val wordMatcher = wordPattern.matcher(code)
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
