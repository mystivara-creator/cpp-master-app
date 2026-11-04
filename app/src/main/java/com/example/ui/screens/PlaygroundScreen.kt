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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CppCodeEditor
import com.example.ui.components.TerminalOutputView
import com.example.ui.theme.CppBluePrimary
import com.example.ui.theme.CppNavyBorder
import com.example.ui.theme.CppNavyCard
import com.example.ui.theme.CppNavyDark
import com.example.ui.theme.CppNavySurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CppNavyDark)
            .testTag("playground_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Playground C++",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CppBluePrimary
                        )
                    )
                    Text(
                        text = "Tulis kode bebas, coba template, & jalankan",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CppNavyCard
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = null,
                            tint = CppBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "g++ C++20",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CppBluePrimary,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
            }
        }

        // Templates Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Pilih Template Cepat:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TEMPLATES.forEach { tmpl ->
                        Surface(
                            onClick = { viewModel.loadPlaygroundTemplate(tmpl.name, tmpl.code) },
                            shape = RoundedCornerShape(10.dp),
                            color = CppNavySurface,
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(CppNavyBorder)
                            )
                        ) {
                            Text(
                                text = tmpl.name,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Code Editor
        item {
            CppCodeEditor(
                code = code,
                onCodeChange = { viewModel.updatePlaygroundCode(it) },
                title = "playground_main.cpp",
                minHeight = 220,
                maxHeight = 360,
                onReset = {
                    viewModel.loadPlaygroundTemplate("Halo Dunia", TEMPLATES[0].code)
                }
            )
        }

        // Optional Standard Input Field
        item {
            OutlinedTextField(
                value = input,
                onValueChange = { viewModel.updatePlaygroundInput(it) },
                label = { Text("Masukan Standar (std::cin) - Opsional", color = TextSecondary, fontSize = 12.sp) },
                placeholder = { Text("Contoh masukan jika program menggunakan cin...", color = TextSecondary.copy(alpha = 0.5f)) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CppBluePrimary,
                    unfocusedBorderColor = CppNavyBorder,
                    focusedContainerColor = CppNavySurface,
                    unfocusedContainerColor = CppNavySurface,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth().testTag("playground_input_field")
            )
        }

        // Run Button
        item {
            Button(
                onClick = { viewModel.runPlayground() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("run_playground_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CppBluePrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Jalankan Program C++ (Run)",
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.Bold
                )
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
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
