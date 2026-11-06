package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.CppCodeEditor
import com.example.ui.components.TerminalOutputView
import com.example.ui.viewmodel.CppViewModel

data class CodeTemplate(val name: String, val code: String)

val TEMPLATES = listOf(
    CodeTemplate(
        "Halo Dunia",
        """#include <iostream>

int main() {
    std::cout << "Halo dari C++ Playground!\n";
    std::cout << "Belajar C++ itu menyenangkan dan bertenaga!\n";
    return 0;
}"""
    ),
    CodeTemplate(
        "Deret Fibonacci",
        """#include <iostream>

int main() {
    int n = 8;
    int t1 = 0, t2 = 1, berikutnya;

    std::cout << "Deret Fibonacci (" << n << " suku): ";
    for (int i = 1; i <= n; ++i) {
        std::cout << t1 << " ";
        berikutnya = t1 + t2;
        t1 = t2;
        t2 = berikutnya;
    }
    std::cout << "\n";
    return 0;
}"""
    ),
    CodeTemplate(
        "Eksperimen Pointer",
        """#include <iostream>

int main() {
    int skor = 85;
    int* ptr = &skor;

    std::cout << "Nilai awal skor: " << skor << "\n";
    std::cout << "Alamat memori: " << ptr << "\n";

    // Modifikasi lewat pointer
    *ptr = 100;
    std::cout << "Nilai skor setelah diubah pointer: " << skor << "\n";
    return 0;
}"""
    ),
    CodeTemplate(
        "Class OOP Sederhana",
        """#include <iostream>
#include <string>

class Mahasiswa {
private:
    std::string nama;
    double ipk;

public:
    Mahasiswa(std::string n, double i) : nama(n), ipk(i) {}

    void sapa() {
        std::cout << "Halo, saya " << nama << " dengan IPK: " << ipk << "\n";
    }
};

int main() {
    Mahasiswa m1("Ahmad", 3.85);
    m1.sapa();
    return 0;
}"""
    ),
    CodeTemplate(
        "Vector & Sorting STL",
        """#include <iostream>
#include <vector>
#include <algorithm>

int main() {
    std::vector<int> angka = {64, 34, 25, 12, 22, 11, 90};

    std::cout << "Sebelum diurutkan: ";
    for (int x : angka) std::cout << x << " ";
    std::cout << "\n";

    std::sort(angka.begin(), angka.end());

    std::cout << "Setelah diurutkan (std::sort): ";
    for (int x : angka) std::cout << x << " ";
    std::cout << "\n";
    return 0;
}"""
    ),
    CodeTemplate(
        "Lambda & Filter",
        """#include <iostream>
#include <vector>
#include <algorithm>

int main() {
    std::vector<int> nilai = {12, 5, 8, 20, 15, 30};

    // Lambda untuk hitung angka > 10
    int lebihDariSepuluh = std::count_if(nilai.begin(), nilai.end(), [](int x) {
        return x > 10;
    });

    std::cout << "Jumlah angka > 10 adalah: " << lebihDariSepuluh << "\n";
    return 0;
}"""
    )
)

@Composable
fun PlaygroundScreen(
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    val code by viewModel.playgroundCode.collectAsState()
    val input by viewModel.playgroundInput.collectAsState()
    val result by viewModel.playgroundResult.collectAsState()
    val isRunning by viewModel.isPlaygroundRunning.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .testTag("playground_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Minimalist Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Playground C++",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = "Eksperimen kode & algoritma secara langsung",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = colorScheme.secondaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = null,
                            tint = colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "C++20",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSecondaryContainer
                            )
                        )
                    }
                }
            }
        }

        // Templates Row with Material 3 SuggestionChip
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Template Kode",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TEMPLATES.forEach { tmpl ->
                        SuggestionChip(
                            onClick = { viewModel.loadPlaygroundTemplate(tmpl.name, tmpl.code) },
                            label = {
                                Text(
                                    text = tmpl.name,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        )
                    }
                }
            }
        }

        // Code Editor
        item {
            CppCodeEditor(
                code = code,
                onCodeChange = { viewModel.updatePlaygroundCode(it) },
                title = "main.cpp",
                minHeight = 220,
                maxHeight = 360,
                onReset = {
                    viewModel.loadPlaygroundTemplate("Halo Dunia", TEMPLATES[0].code)
                }
            )
        }

        // Standard Input Field
        item {
            OutlinedTextField(
                value = input,
                onValueChange = { viewModel.updatePlaygroundInput(it) },
                label = { Text("Masukan Standar (std::cin) — Opsional") },
                placeholder = { Text("Masukkan nilai input jika kode menggunakan std::cin") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("playground_input_field")
            )
        }

        // Run Button
        item {
            Button(
                onClick = { viewModel.runPlayground() },
                enabled = !isRunning,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("run_playground_btn"),
                shape = RoundedCornerShape(24.dp)
            ) {
                if (isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Menjalankan...", style = MaterialTheme.typography.labelLarge)
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Jalankan Program",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // Output Terminal
        item {
            TerminalOutputView(
                output = result?.output ?: "",
                executionTimeMs = result?.executionTimeMs,
                isSuccess = result?.isSuccess ?: true
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
