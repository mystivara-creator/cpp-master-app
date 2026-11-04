package com.example.ui.components

import android.widget.Toast
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.CppBluePrimary
import com.example.ui.theme.CppCodeBackground
import com.example.ui.theme.CppCodeLineNumber
import com.example.ui.theme.CppNavyBorder
import com.example.ui.theme.CppNavyCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CppCodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Editor C++ (main.cpp)",
    readOnly: Boolean = false,
    minHeight: Int = 180,
    maxHeight: Int = 400,
    onReset: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val horizontalScrollState = rememberScrollState()

    val lineCount = remember(code) {
        maxOf(1, code.count { it == '\n' } + 1)
    }

    val lineNumbersText = remember(lineCount) {
        (1..lineCount).joinToString("\n")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CppCodeBackground)
    ) {
        // Top Bar (Terminal Window style)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CppNavyCard)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Window dots
                Box(Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFFEF4444)))
                Box(Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFFF59E0B)))
                Box(Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFF10B981)))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onReset != null && !readOnly) {
                    IconButton(
                        onClick = onReset,
                        modifier = Modifier.size(32.dp).testTag("editor_reset_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset Kode",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(code))
                        Toast.makeText(context, "Kode disalin ke clipboard", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp).testTag("editor_copy_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Salin Kode",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Code Body with Line Numbers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight.dp, max = maxHeight.dp)
                .padding(8.dp)
        ) {
            // Line numbers column
            Text(
                text = lineNumbersText,
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = CppCodeLineNumber,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .width(36.dp)
                    .padding(end = 8.dp)
            )

            // Code text or editor
            Box(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(horizontalScrollState)
            ) {
                if (readOnly) {
                    val highlightedCode = remember(code) { CppSyntaxHighlighter.highlightCppCode(code) }
                    Text(
                        text = highlightedCode,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
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
                            color = TextPrimary
                        ),
                        cursorBrush = SolidColor(CppBluePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("code_editor_input")
                    )
                }
            }
        }

        // Quick Symbol Toolbar (shown only in editable mode)
        if (!readOnly) {
            QuickSymbolsBar(
                onInsertSymbol = { symbol ->
                    onCodeChange(code + symbol)
                }
            )
        }
    }
}

@Composable
fun QuickSymbolsBar(
    onInsertSymbol: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val quickSymbols = listOf(
        "    ", "{", "}", "(", ")", ";", "<<", ">>", "::", "*", "&",
        "\"", "'", "=", "std::", "cout", "cin", "endl", "vector", "int"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CppNavyCard.copy(alpha = 0.8f))
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        quickSymbols.forEach { sym ->
            Surface(
                onClick = { onInsertSymbol(sym) },
                shape = RoundedCornerShape(6.dp),
                color = CppNavyBorder,
                modifier = Modifier.height(28.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (sym == "    ") "Tab" else sym,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = CppBluePrimary
                        )
                    )
                }
            }
        }
    }
}
