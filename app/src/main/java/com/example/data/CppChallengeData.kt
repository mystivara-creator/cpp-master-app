package com.example.data

import com.example.data.model.ChallengeDifficulty
import com.example.data.model.DailyChallenge
import com.example.data.model.TestCase
import java.util.Calendar

object CppChallengeData {
    val challenges: List<DailyChallenge> = listOf(
        DailyChallenge(
            id = "ch_day_1",
            dayNumber = 1,
            title = "Pembalik Kata & Penghitung Vokal",
            difficulty = ChallengeDifficulty.MUDAH,
            tags = listOf("String", "Loop", "Dasar"),
            description = "Buatlah program C++ yang menerima satu kata masukan, lalu mencetak kata tersebut dalam keadaan terbalik diikuti dengan jumlah huruf vokal (a, i, u, e, o baik huruf kecil maupun kapital) yang ada di dalamnya dipisahkan oleh spasi.",
            inputSpecification = "Satu string satu kata tanpa spasi.",
            outputSpecification = "String terbalik diikuti spasi dan integer jumlah huruf vokal.",
            exampleInput = "cppmaster",
            exampleOutput = "retsamppc 2",
            starterCode = """#include <iostream>
#include <string>
#include <algorithm>

int main() {
    std::string kata;
    if (std::cin >> kata) {
        // Tulis kode Anda di sini:
        // 1. Balik kata
        // 2. Hitung jumlah vokal
        // 3. Cetak: <kata_terbalik> <jumlah_vokal>
        
    }
    return 0;
}""",
            testCases = listOf(
                TestCase("Kata biasa", "cppmaster", "retsamppc 2"),
                TestCase("Kata pendek", "halo", "olah 2"),
                TestCase("Huruf kapital", "Algoritma", "amtiroglA 4"),
                TestCase("Tanpa vokal", "rhythm", "mhtyhr 0", isHidden = true)
            ),
            hints = listOf(
                "Anda dapat menggunakan std::reverse(kata.begin(), kata.end()) dari <algorithm> atau loop for terbalik.",
                "Periksa vokal dengan mengecek: c == 'a' || c == 'i' || c == 'u' || c == 'e' || c == 'o' setelah tolower()."
            ),
            solutionCode = """#include <iostream>
#include <string>
#include <algorithm>
#include <cctype>

int main() {
    std::string kata;
    if (std::cin >> kata) {
        int vokal = 0;
        for (char c : kata) {
            char lower = std::tolower(c);
            if (lower == 'a' || lower == 'i' || lower == 'u' || lower == 'e' || lower == 'o') {
                vokal++;
            }
        }
        std::string terbalik = kata;
        std::reverse(terbalik.begin(), terbalik.end());
        std::cout << terbalik << " " << vokal << "\n";
    }
    return 0;
}""",
            solutionExplanation = "Kita menghitung huruf vokal dengan loop range-based, lalu membalik string menggunakan algoritma std::reverse bawaan STL."
        ),

        DailyChallenge(
            id = "ch_day_2",
            dayNumber = 2,
            title = "Pengecekan Bilangan Prima",
            difficulty = ChallengeDifficulty.MUDAH,
            tags = listOf("Matematika", "Percabangan", "Dasar"),
            description = "Diberikan sebuah bilangan bulat positif N. Buatlah program untuk menentukan apakah bilangan tersebut adalah bilangan prima. Cetak 'PRIMA' jika ya, atau 'BUKAN' jika bukan.",
            inputSpecification = "Satu bilangan bulat N (1 <= N <= 10000).",
            outputSpecification = "String 'PRIMA' atau 'BUKAN'.",
            exampleInput = "17",
            exampleOutput = "PRIMA",
            starterCode = """#include <iostream>

int main() {
    int n;
    if (std::cin >> n) {
        // Periksa apakah n prima
        // Cetak "PRIMA" atau "BUKAN"
    }
    return 0;
}""",
            testCases = listOf(
                TestCase("Angka prima 17", "17", "PRIMA"),
                TestCase("Angka genap bukan prima", "4", "BUKAN"),
                TestCase("Angka 1 bukan prima", "1", "BUKAN"),
                TestCase("Angka prima 2", "2", "PRIMA"),
                TestCase("Angka prima besar 97", "97", "PRIMA", isHidden = true)
            ),
            hints = listOf(
                "Bilangan prima adalah bilangan lebih besar dari 1 yang hanya habis dibagi 1 dan dirinya sendiri.",
                "Anda hanya perlu mengecek pembagi dari 2 hingga akar kuadrat dari N (i * i <= n)."
            ),
            solutionCode = """#include <iostream>

int main() {
    int n;
    if (std::cin >> n) {
        if (n <= 1) {
            std::cout << "BUKAN\n";
            return 0;
        }
        bool prima = true;
        for (int i = 2; i * i <= n; ++i) {
            if (n % i == 0) {
                prima = false;
                break;
            }
        }
        std::cout << (prima ? "PRIMA" : "BUKAN") << "\n";
    }
    return 0;
}""",
            solutionExplanation = "Looping sampai sqrt(N) efisien dengan kompleksitas O(sqrt(N)) untuk menguji faktor pembagi."
        ),

        DailyChallenge(
            id = "ch_day_3",
            dayNumber = 3,
            title = "Penjumlahan Elemen Terbesar Kedua",
            difficulty = ChallengeDifficulty.SEDANG,
            tags = listOf("Array", "Vector", "Sorting"),
            description = "Diberikan sebuah deretan angka dipisahkan oleh spasi dengan jumlah N elemen di awal baris. Temukan elemen terbesar kedua di dalam deretan tersebut, lalu cetak nilai tersebut.",
            inputSpecification = "Baris pertama berisi N (2 <= N <= 100). Baris berikutnya berisi N bilangan bulat.",
            outputSpecification = "Satu bilangan bulat elemen terbesar kedua unik.",
            exampleInput = "5 10 40 30 20 40",
            exampleOutput = "30",
            starterCode = """#include <iostream>
#include <vector>
#include <algorithm>

int main() {
    int n;
    if (std::cin >> n) {
        std::vector<int> angka(n);
        for (int i = 0; i < n; ++i) {
            std::cin >> angka[i];
        }
        // Cari elemen terbesar kedua unik
    }
    return 0;
}""",
            testCases = listOf(
                TestCase("Contoh standar", "5 10 40 30 20 40", "30"),
                TestCase("Deret 3 angka", "3 5 2 8", "5"),
                TestCase("Ada duplikat terbesar", "4 100 100 50 20", "50"),
                TestCase("Angka negatif", "3 -10 -5 -20", "-10", isHidden = true)
            ),
            hints = listOf(
                "Hati-hati dengan elemen duplikat! Jika angka terbesar muncul dua kali (misal: 40, 40), terbesar kedua adalah nilai di bawahnya (30).",
                "Anda dapat mengurutkan secara descending dengan std::sort, lalu cari elemen pertama yang bernilai lebih kecil dari elemen puncak."
            ),
            solutionCode = """#include <iostream>
#include <vector>
#include <algorithm>

int main() {
    int n;
    if (std::cin >> n) {
        std::vector<int> angka(n);
        for (int i = 0; i < n; ++i) {
            std::cin >> angka[i];
        }
        std::sort(angka.rbegin(), angka.rend());
        int terbesar = angka[0];
        int terbesarKedua = terbesar;
        for (int x : angka) {
            if (x < terbesar) {
                terbesarKedua = x;
                break;
            }
        }
        std::cout << terbesarKedua << "\n";
    }
    return 0;
}""",
            solutionExplanation = "Mengurutkan secara menurun rbegin()-rend() memudahkan mencari angka pertama yang nilainya strictly less than nilai maksimum."
        ),

        DailyChallenge(
            id = "ch_day_4",
            dayNumber = 4,
            title = "Penukaran Nilai dengan Pointer",
            difficulty = ChallengeDifficulty.MUDAH,
            tags = listOf("Pointer", "Fungsi", "Menengah"),
            description = "Buatlah fungsi C++ 'void swapPointer(int* a, int* b)' yang menukar nilai dua variabel integer di memori menggunakan pointer. Cetak nilai kedua variabel setelah ditukar.",
            inputSpecification = "Dua bilangan bulat A dan B dipisahkan spasi.",
            outputSpecification = "Dua bilangan bulat setelah ditukar dipisahkan spasi.",
            exampleInput = "10 99",
            exampleOutput = "99 10",
            starterCode = """#include <iostream>

void swapPointer(int* a, int* b) {
    // Implementasikan pertukaran pointer
}

int main() {
    int x, y;
    if (std::cin >> x >> y) {
        swapPointer(&x, &y);
        std::cout << x << " " << y << "\n";
    }
    return 0;
}""",
            testCases = listOf(
                TestCase("Nilai positif", "10 99", "99 10"),
                TestCase("Nilai negatif dan nol", "-5 0", "0 -5"),
                TestCase("Nilai sama", "7 7", "7 7")
            ),
            hints = listOf(
                "Gunakan variabel sementara (int temp = *a;) untuk menyimpan nilai dereference pertama.",
                "Perbarui nilai yang ditunjuk dengan *a = *b; lalu *b = temp;"
            ),
            solutionCode = """#include <iostream>

void swapPointer(int* a, int* b) {
    if (a != nullptr && b != nullptr) {
        int temp = *a;
        *a = *b;
        *b = temp;
    }
}

int main() {
    int x, y;
    if (std::cin >> x >> y) {
        swapPointer(&x, &y);
        std::cout << x << " " << y << "\n";
    }
    return 0;
}""",
            solutionExplanation = "Pointer dereference (*) mengakses langsung memori stack variabel asli x dan y."
        ),

        DailyChallenge(
            id = "ch_day_5",
            dayNumber = 5,
            title = "Validasi Tanda Kurung Berpasangan",
            difficulty = ChallengeDifficulty.SEDANG,
            tags = listOf("Stack", "Data Structure", "Menengah"),
            description = "Diberikan sebuah string tanda kurung yang hanya terdiri dari '(', ')', '{', '}', '[', ']'. Tentukan apakah urutan kurung tersebut valid dan berpasangan dengan benar. Cetak 'VALID' jika ya, atau 'TIDAK' jika tidak.",
            inputSpecification = "Satu baris string tanda kurung.",
            outputSpecification = "'VALID' atau 'TIDAK'.",
            exampleInput = "{[()]}",
            exampleOutput = "VALID",
            starterCode = """#include <iostream>
#include <string>
#include <vector>

bool isValid(const std::string& s) {
    // Gunakan std::vector sebagai stack
    return true;
}

int main() {
    std::string s;
    if (std::cin >> s) {
        std::cout << (isValid(s) ? "VALID" : "TIDAK") << "\n";
    }
    return 0;
}""",
            testCases = listOf(
                TestCase("Kurung bersarang benar", "{[()]}", "VALID"),
                TestCase("Kurung bertumpuk salah", "([)]", "TIDAK"),
                TestCase("Hanya kurung buka", "((", "TIDAK"),
                TestCase("Pasangan berulang", "()[]{}", "VALID"),
                TestCase("Kurung tutup di awal", "]", "TIDAK", isHidden = true)
            ),
            hints = listOf(
                "Push karakter pembuka ke stack.",
                "Saat menemukan karakter penutup, periksa apakah stack tidak kosong dan karakter teratas adalah pasangannya."
            ),
            solutionCode = """#include <iostream>
#include <string>
#include <vector>

bool isValid(const std::string& s) {
    std::vector<char> stack;
    for (char c : s) {
        if (c == '(' || c == '{' || c == '[') {
            stack.push_back(c);
        } else {
            if (stack.empty()) return false;
            char top = stack.back();
            stack.pop_back();
            if ((c == ')' && top != '(') ||
                (c == '}' && top != '{') ||
                (c == ']' && top != '[')) {
                return false;
            }
        }
    }
    return stack.empty();
}

int main() {
    std::string s;
    if (std::cin >> s) {
        std::cout << (isValid(s) ? "VALID" : "TIDAK") << "\n";
    }
    return 0;
}""",
            solutionExplanation = "Menggunakan vector sebagai LIFO stack untuk memverifikasi kecocokan bracket secara linear O(N)."
        ),

        DailyChallenge(
            id = "ch_day_6",
            dayNumber = 6,
            title = "Penghitung Frekuensi Kata (std::map)",
            difficulty = ChallengeDifficulty.SEDANG,
            tags = listOf("STL", "Map", "Mahir"),
            description = "Diberikan N kata. Hitung frekuensi kemunculan setiap kata, lalu cetak setiap kata unik beserta jumlah kemunculannya secara alfabetis (format: kata: jumlah).",
            inputSpecification = "Integer N diikuti oleh N kata dipisahkan spasi.",
            outputSpecification = "Setiap baris mencetak: <kata>: <jumlah>",
            exampleInput = "6 apel jeruk apel pisang jeruk apel",
            exampleOutput = "apel: 3\njeruk: 2\npisang: 1",
            starterCode = """#include <iostream>
#include <string>
#include <map>

int main() {
    int n;
    if (std::cin >> n) {
        std::map<std::string, int> frekuensi;
        // Baca n kata dan simpan frekuensinya
        // Cetak hasilnya terurut
    }
    return 0;
}""",
            testCases = listOf(
                TestCase("Kata berulang", "6 apel jeruk apel pisang jeruk apel", "apel: 3\njeruk: 2\npisang: 1"),
                TestCase("Kata tunggal", "3 kopi susu kopi", "kopi: 2\nsusu: 1")
            ),
            hints = listOf(
                "std::map secara alami mengurutkan kunci (key) berdasarkan urutan leksikografis (alfabetis).",
                "Gunakan 'frekuensi[kata]++' untuk menambah hitungan secara otomatis."
            ),
            solutionCode = """#include <iostream>
#include <string>
#include <map>

int main() {
    int n;
    if (std::cin >> n) {
        std::map<std::string, int> frekuensi;
        for (int i = 0; i < n; ++i) {
            std::string kata;
            std::cin >> kata;
            frekuensi[kata]++;
        }
        for (const auto& pair : frekuensi) {
            std::cout << pair.first << ": " << pair.second << "\n";
        }
    }
    return 0;
}""",
            solutionExplanation = "std::map mengurutkan kunci secara otomatis dengan pohon biner terimbang (Red-Black Tree)."
        ),

        DailyChallenge(
            id = "ch_day_7",
            dayNumber = 7,
            title = "Class Rekening & Validasi Transaksi (OOP)",
            difficulty = ChallengeDifficulty.SEDANG,
            tags = listOf("OOP", "Enkapsulasi", "Menengah"),
            description = "Buat class Rekening dengan saldo awal. Program menerima perintah transaksi: 'S <jumlah>' untuk Setor, atau 'T <jumlah>' untuk Tarik. Jika penarikan melebihi saldo, cetak 'SALDO_KURANG'. Di akhir seluruh perintah, cetak sisa saldo.",
            inputSpecification = "Saldo awal N, jumlah perintah K, lalu K baris instruksi transaksi.",
            outputSpecification = "Pesan 'SALDO_KURANG' jika gagal tarik, dan baris terakhir 'SALDO: <jumlah>'.",
            exampleInput = "1000 3\nS 500\nT 2000\nT 300",
            exampleOutput = "SALDO_KURANG\nSALDO: 1200",
            starterCode = """#include <iostream>

class Rekening {
private:
    int saldo;
public:
    Rekening(int s) : saldo(s) {}
    void setor(int j) { saldo += j; }
    bool tarik(int j) {
        if (j <= saldo) {
            saldo -= j;
            return true;
        }
        return false;
    }
    int getSaldo() const { return saldo; }
};

int main() {
    int saldoAwal, k;
    if (std::cin >> saldoAwal >> k) {
        Rekening rek(saldoAwal);
        // Proses k perintah
        // Cetak saldo akhir
    }
    return 0;
}""",
            testCases = listOf(
                TestCase("Transaksi campur", "1000 3 S 500 T 2000 T 300", "SALDO_KURANG\nSALDO: 1200"),
                TestCase("Semua berhasil", "500 2 S 100 T 200", "SALDO: 400")
            ),
            hints = listOf(
                "Baca tipe char 'S' atau 'T' diikuti integer jumlah.",
                "Jika fungsi tarik mengembalikan false, segera cetak 'SALDO_KURANG'."
            ),
            solutionCode = """#include <iostream>

class Rekening {
private:
    int saldo;
public:
    Rekening(int s) : saldo(s) {}
    void setor(int j) { saldo += j; }
    bool tarik(int j) {
        if (j <= saldo) {
            saldo -= j;
            return true;
        }
        return false;
    }
    int getSaldo() const { return saldo; }
};

int main() {
    int saldoAwal, k;
    if (std::cin >> saldoAwal >> k) {
        Rekening rek(saldoAwal);
        for (int i = 0; i < k; ++i) {
            char tipe;
            int jumlah;
            std::cin >> tipe >> jumlah;
            if (tipe == 'S') {
                rek.setor(jumlah);
            } else if (tipe == 'T') {
                if (!rek.tarik(jumlah)) {
                    std::cout << "SALDO_KURANG\n";
                }
            }
        }
        std::cout << "SALDO: " << rek.getSaldo() << "\n";
    }
    return 0;
}""",
            solutionExplanation = "Menerapkan enkapsulasi data dengan metode getter dan setter yang memvalidasi kondisi saldo aman."
        ),

        DailyChallenge(
            id = "ch_day_8",
            dayNumber = 8,
            title = "Template Fungsi Nilai Maksimum (Generic)",
            difficulty = ChallengeDifficulty.SULIT,
            tags = listOf("Templates", "Generic", "Mahir"),
            description = "Buat template fungsi 'cariTerbesar(T a, T b, T c)' yang mengembalikan nilai terbesar dari 3 nilai dengan tipe generik T. Program menerima 3 bilangan double dan mencetak nilai terbesarnya.",
            inputSpecification = "Tiga bilangan desimal (double) dipisahkan spasi.",
            outputSpecification = "Satu bilangan nilai terbesar.",
            exampleInput = "3.14 9.81 2.71",
            exampleOutput = "9.81",
            starterCode = """#include <iostream>

template <typename T>
T cariTerbesar(T a, T b, T c) {
    // Tulis logika template
    return a;
}

int main() {
    double x, y, z;
    if (std::cin >> x >> y >> z) {
        std::cout << cariTerbesar(x, y, z) << "\n";
    }
    return 0;
}""",
            testCases = listOf(
                TestCase("Tiga nilai double", "3.14 9.81 2.71", "9.81"),
                TestCase("Nilai negatif", "-10.5 -2.1 -50.0", "-2.1"),
                TestCase("Nilai sama", "5.0 5.0 5.0", "5")
            ),
            hints = listOf(
                "Gunakan perbandingan bertahap: T maks = a; if (b > maks) maks = b; if (c > maks) maks = c; return maks;"
            ),
            solutionCode = """#include <iostream>

template <typename T>
T cariTerbesar(T a, T b, T c) {
    T maks = a;
    if (b > maks) maks = b;
    if (c > maks) maks = c;
    return maks;
}

int main() {
    double x, y, z;
    if (std::cin >> x >> y >> z) {
        std::cout << cariTerbesar(x, y, z) << "\n";
    }
    return 0;
}""",
            solutionExplanation = "Template generic memungkinkan fungsi bekerja untuk int, float, double, char, maupun std::string."
        )
    )

    fun getTodayChallenge(): DailyChallenge {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % challenges.size
        return challenges[index]
    }

    fun getChallengeForDay(day: Int): DailyChallenge {
        val index = (day - 1) % challenges.size
        return challenges[index]
    }
}
