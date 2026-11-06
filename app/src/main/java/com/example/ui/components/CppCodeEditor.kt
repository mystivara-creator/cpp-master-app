package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CppCodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "main.cpp",
    readOnly: Boolean = false,
    minHeight: Int = 180,
    maxHeight: Int = 400,
    onReset: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val horizontalScrollState = rememberScrollState()
    val colorScheme = MaterialTheme.colorScheme

    val lineCount = remember(code) {
        maxOf(1, code.count { it == '\n' } + 1)
    }

    val lineNumbersText = remember(lineCount) {
        (1..lineCount).joinToString("\n")
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = colorScheme.surfaceContainerLow
        ),
        border = BorderStroke(1.dp, colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Minimalist Google-style Code Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surfaceContainer)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            color = colorScheme.onSurfaceVariant
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onReset != null && !readOnly) {
                        IconButton(
                            onClick = onReset,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("editor_reset_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset Kode",
                                tint = colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(code))
                            Toast.makeText(context, "Kode disalin ke clipboard", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("editor_copy_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Salin Kode",
                            tint = colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.6f))

            // Code Body with Line Numbers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight.dp, max = maxHeight.dp)
                    .padding(12.dp)
            ) {
                Text(
                    text = lineNumbersText,
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier
                        .width(32.dp)
                        .padding(end = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(horizontalScrollState)
                ) {
                    if (readOnly) {
                        val highlightedCode = remember(code, colorScheme.onSurface, colorScheme.primary) {
                            CppSyntaxHighlighter.highlightCppCode(
                                code = code,
                                baseTextColor = colorScheme.onSurface,
                                keywordColor = colorScheme.primary,
                                commentColor = colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = highlightedCode,
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = colorScheme.onSurface
                            )
                        )
                    } else {
                        BasicTextField(
                            value = code,
                            onValueChange = onCodeChange,
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = colorScheme.onSurface
                            ),
                            cursorBrush = SolidColor(colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("code_editor_input")
                        )
                    }
                }
            }

            if (!readOnly) {
                HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.6f))
                QuickSymbolsBar(
                    onInsertSymbol = { symbol ->
                        onCodeChange(code + symbol)
                    }
                )
            }
        }
    }
}

@Composable
fun QuickSymbolsBar(
    onInsertSymbol: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val quickSymbols = remember {
        listOf(
            "    ", "{", "}", "(", ")", ";", "<<", ">>", "::", "*", "&",
            "\"", "'", "=", "std::", "cout", "cin", "endl", "vector", "int"
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.surfaceContainer)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        quickSymbols.forEach { sym ->
            Surface(
                onClick = { onInsertSymbol(sym) },
                shape = RoundedCornerShape(6.dp),
                color = colorScheme.surfaceContainerHighest,
                modifier = Modifier.height(28.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (sym == "    ") "Tab" else sym,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}
