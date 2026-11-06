package com.example.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

object CppSyntaxHighlighter {
    private val preprocessorRegex = Regex("(#include\\s*[<\"][^>\"]*[>\"]|#define\\s+\\w+|#pragma\\s+\\w+)")
    private val keywordRegex = Regex(
        "\\b(int|long|short|float|double|char|bool|void|auto|const|constexpr|static|struct|class|public|private|protected|virtual|override|template|typename|namespace|using|return|if|else|switch|case|default|for|while|do|break|continue|new|delete|nullptr|true|false|try|catch|throw|sizeof)\\b"
    )
    private val stdLibRegex = Regex(
        "\\b(std|cout|cin|endl|string|vector|map|unordered_map|set|pair|unique_ptr|shared_ptr|make_unique|make_shared|sort|find|push_back|size|empty|begin|end|getline)\\b"
    )
    private val numberRegex = Regex("\\b\\d+(\\.\\d+)?f?\\b")
    private val stringRegex = Regex("\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'")
    private val commentRegex = Regex("//.*|/\\*[\\s\\S]*?\\*/")

    fun highlightCppCode(
        code: String,
        baseTextColor: Color = Color(0xFFE3E2E6),
        keywordColor: Color = Color(0xFF0B57D0),
        commentColor: Color = Color(0xFF74777F)
    ): AnnotatedString {
        return buildAnnotatedString {
            append(code)
            addStyle(SpanStyle(color = baseTextColor), 0, code.length)

            keywordRegex.findAll(code).forEach { match ->
                addStyle(
                    SpanStyle(color = keywordColor, fontWeight = FontWeight.SemiBold),
                    match.range.first,
                    match.range.last + 1
                )
            }

            stdLibRegex.findAll(code).forEach { match ->
                addStyle(
                    SpanStyle(color = keywordColor.copy(alpha = 0.85f), fontWeight = FontWeight.Medium),
                    match.range.first,
                    match.range.last + 1
                )
            }

            numberRegex.findAll(code).forEach { match ->
                addStyle(
                    SpanStyle(color = baseTextColor, fontWeight = FontWeight.Medium),
                    match.range.first,
                    match.range.last + 1
                )
            }

            preprocessorRegex.findAll(code).forEach { match ->
                addStyle(
                    SpanStyle(color = keywordColor, fontWeight = FontWeight.Medium),
                    match.range.first,
                    match.range.last + 1
                )
            }

            stringRegex.findAll(code).forEach { match ->
                addStyle(
                    SpanStyle(color = baseTextColor.copy(alpha = 0.9f)),
                    match.range.first,
                    match.range.last + 1
                )
            }

            commentRegex.findAll(code).forEach { match ->
                addStyle(
                    SpanStyle(color = commentColor),
                    match.range.first,
                    match.range.last + 1
                )
            }
        }
    }
}
