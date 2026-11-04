package com.example.data

import com.example.data.model.CourseLevel
import com.example.data.model.Lesson
import com.example.data.model.LessonSection
import com.example.data.model.QuizQuestion

object CppCurriculumData {
    val lessons: List<Lesson> = listOf(
        // ================= DASAR =================
        Lesson(
            id = "d1_intro",
            level = CourseLevel.DASAR,
            order = 1,
            title = "Pengenalan C++ & Struktur Program",
            subtitle = "Memahami anatomi kode C++, main(), #include, dan cout/cin",
            estimatedMinutes = 8,
            summary = "C++ adalah bahasa berkinerja tinggi yang dikembangkan oleh Bjarne Stroustrup. Pelajari struktur dasar setiap program C++.",
            sections = listOf(
                LessonSection.Heading("Apa Itu C++?"),
                LessonSection.Paragraph(
                    "C++ adalah bahasa pemrograman tingkat menengah-ke-tinggi yang mendukung paradigma prosedural, berorientasi objek (OOP), dan generic. C++ sangat populer dalam pengembangan game engine (Unreal Engine), sistem operasi, driver perangkat keras, aplikasi finansial berkecepatan tinggi, dan kecerdasan buatan."
                ),
                LessonSection.Heading("Struktur Dasar Program C++"),
                LessonSection.Paragraph(
                    "Setiap program C++ yang dapat dieksekusi memiliki minimal satu fungsi utama yang bernama main(). Program dimulai dan berakhir di fungsi ini."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    // Menampilkan teks ke terminal
    std::cout << "Halo Dunia C++!" << std::endl;
    std::cout << "Selamat belajar dari dasar hingga mahir." << std::endl;
    return 0; // Mengindikasikan program selesai sukses
}""",
                    output = "Halo Dunia C++!\nSelamat belajar dari dasar hingga mahir.",
                    explanation = "#include <iostream> mengimpor pustaka input/output stream. std::cout digunakan untuk mencetak ke konsol, dan operator << mengirim data ke stream."
                ),
                LessonSection.Heading("Mengambil Masukan (std::cin)"),
                LessonSection.Paragraph(
                    "Untuk menerima masukan dari pengguna melalui keyboard, kita menggunakan objek std::cin bersama dengan operator ekstraksi >>."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    int umur;
    std::cout << "Masukkan umur Anda: ";
    std::cin >> umur;
    std::cout << "Umur Anda adalah: " << umur << " tahun" << std::endl;
    return 0;
}""",
                    output = "Masukkan umur Anda: 21\nUmur Anda adalah: 21 tahun",
                    explanation = "std::cin membaca masukan sampai menemukan spasi atau baris baru."
                ),
                LessonSection.TipBox(
                    title = "Gunakan '\\n' vs std::endl",
                    message = "std::endl tidak hanya membuat baris baru, tetapi juga memaksa flush buffer memori. Dalam loop performa tinggi, gunakan '\\n' karena jauh lebih cepat!"
                ),
                LessonSection.WarningBox(
                    title = "Hindari 'using namespace std;' di Header File",
                    message = "Menulis 'using namespace std;' di header file dapat menyebabkan namespace pollution dan konflik nama variabel di proyek besar. Biasakan menulis std::cout atau gunakan di scope lokal."
                ),
                LessonSection.KeyPoints(
                    listOf(
                        "Setiap statement di C++ diakhiri tanda titik-koma (;)",
                        "Titik awal eksekusi program selalu fungsi int main()",
                        "#include menyertakan pustaka standar dari C++ STL",
                        "std::cout untuk output dan std::cin untuk input"
                    )
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_d1",
                category = "Struktur C++",
                level = CourseLevel.DASAR,
                question = "Fungsi apakah yang merupakan titik awal eksekusi dalam semua program C++?",
                options = listOf("start()", "main()", "run()", "execute()"),
                correctIndex = 1,
                explanation = "Eksekusi program C++ selalu dimulai dari fungsi global bernama main()."
            )
        ),

        Lesson(
            id = "d2_variables",
            level = CourseLevel.DASAR,
            order = 2,
            title = "Variabel, Tipe Data & Konstanta",
            subtitle = "Tipe primitif, modifier ukuran, const, constexpr, dan auto",
            estimatedMinutes = 10,
            summary = "Ketahui berbagai tipe data di C++ dari integer, floating-point, char, boolean, hingga keyword modern seperti auto dan constexpr.",
            sections = listOf(
                LessonSection.Heading("Tipe Data Primitif"),
                LessonSection.Paragraph(
                    "C++ adalah bahasa strongly-typed dan statically-typed. Ini berarti setiap variabel harus memiliki tipe yang jelas pada saat kompilasi."
                ),
                LessonSection.KeyPoints(
                    listOf(
                        "int: bilangan bulat standar (biasanya 4 byte, -2 milyar s/d +2 milyar)",
                        "double: bilangan desimal presisi ganda (8 byte, 15 digit presisi)",
                        "float: bilangan desimal presisi tunggal (4 byte)",
                        "char: satu karakter huruf/simbol (1 byte)",
                        "bool: nilai boolean (true atau false, 1 byte)",
                        "std::string: teks kalimat/kumpulan karakter (dari pustaka <string>)"
                    )
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <string>

int main() {
    int skor = 1500;
    double ipk = 3.92;
    char grade = 'A';
    bool aktif = true;
    std::string nama = "Budi Hartono";

    std::cout << "Mahasiswa: " << nama << " (" << grade << ")\n";
    std::cout << "IPK: " << ipk << ", Skor: " << skor << "\n";
    return 0;
}""",
                    output = "Mahasiswa: Budi Hartono (A)\nIPK: 3.92, Skor: 1500",
                    explanation = "std::string menggunakan tanda kutip ganda (\" \"), sedangkan char menggunakan tanda kutip tunggal (' ')."
                ),
                LessonSection.Heading("Konstanta: const vs constexpr"),
                LessonSection.Paragraph(
                    "Gunakan 'const' untuk nilai yang tidak boleh diubah setelah diinisialisasi saat runtime. Gunakan 'constexpr' (C++11) jika nilai tersebut sudah pasti dapat dihitung saat waktu kompilasi (compile-time constant)."
                ),
                LessonSection.CodeBlock(
                    code = """constexpr double PI = 3.1415926535;
constexpr int MAX_BUFFER = 1024 * 4; // Dihitung waktu kompilasi!

const int waktuSekarang = hitungWaktu(); // Runtime constant""",
                    output = null,
                    explanation = "constexpr meningkatkan performa aplikasi karena nilai sudah dihitung sebelumnya oleh kompilator."
                ),
                LessonSection.Heading("Tipe Otomatis: keyword auto (C++11)"),
                LessonSection.Paragraph(
                    "Keyword 'auto' memungkinkan kompilator menyimpulkan tipe data secara otomatis dari nilai inisialisasi."
                ),
                LessonSection.CodeBlock(
                    code = """auto x = 42;          // x adalah int
auto y = 3.14;        // y adalah double
auto pesan = "Hello"; // pesan adalah const char*""",
                    output = null,
                    explanation = "Gunakan auto saat tipe data terlalu panjang (misal iterator STL) agar kode tetap bersih dan mudah dibaca."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_d2",
                category = "Tipe Data",
                level = CourseLevel.DASAR,
                question = "Perbedaan mendasar antara 'const' dan 'constexpr' di modern C++ adalah:",
                options = listOf(
                    "constexpr hanya bisa untuk string",
                    "constexpr dievaluasi saat waktu kompilasi (compile-time)",
                    "const tidak menggunakan memori",
                    "Tidak ada perbedaan sama sekali"
                ),
                correctIndex = 1,
                explanation = "constexpr menjamin bahwa ekspresi tersebut dapat dihitung dan dievaluasi saat compile-time."
            )
        ),

        Lesson(
            id = "d3_operators",
            level = CourseLevel.DASAR,
            order = 3,
            title = "Operator Lengkap C++",
            subtitle = "Aritmatika, Perbandingan, Logika, Bitwise, dan Ternary",
            estimatedMinutes = 10,
            summary = "Kuasai operator aritmatika, presedensi, operator logika short-circuit, bitwise, dan ternary operator.",
            sections = listOf(
                LessonSection.Heading("Jenis-Jenis Operator"),
                LessonSection.Paragraph(
                    "Operator adalah simbol yang memerintahkan kompilator untuk melakukan manipulasi matematis atau logis tertentu pada operan."
                ),
                LessonSection.KeyPoints(
                    listOf(
                        "Aritmatika: +, -, *, /, % (modulo sisa bagi)",
                        "Perbandingan: ==, !=, <, >, <=, >=",
                        "Logika: && (AND), || (OR), ! (NOT)",
                        "Bitwise: & (AND bit), | (OR bit), ^ (XOR bit), ~ (NOT bit), <<, >>",
                        "Ternary: kondisi ? ekspresi_benar : ekspresi_salah"
                    )
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    int a = 14;
    int b = 4;

    std::cout << "Pembagian bulat: " << (a / b) << "\n";      // 3
    std::cout << "Sisa bagi (modulo): " << (a % b) << "\n";  // 2

    // Pembagian desimal membutuhkan type casting
    double hasilDesimal = static_cast<double>(a) / b;
    std::cout << "Hasil desimal: " << hasilDesimal << "\n";   // 3.5

    // Ternary operator
    int nilai = 75;
    std::string status = (nilai >= 70) ? "Lulus" : "Remedial";
    std::cout << "Status: " << status << "\n";
    return 0;
}""",
                    output = "Pembagian bulat: 3\nSisa bagi (modulo): 2\nHasil desimal: 3.5\nStatus: Lulus",
                    explanation = "Di C++, pembagian antara dua integer selalu menghasilkan integer (dibulatkan ke bawah). Gunakan static_cast<double>() untuk desimal."
                ),
                LessonSection.TipBox(
                    title = "Short-Circuit Evaluation",
                    message = "Pada operator logika '&&', jika kondisi kiri salah, kondisi kanan tidak akan dieksekusi sama sekali. Begitu juga pada '||', jika kondisi kiri benar, kondisi kanan diabaikan."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_d3",
                category = "Operator",
                level = CourseLevel.DASAR,
                question = "Berapakah hasil dari ekspresi 7 / 2 di C++ jika kedua angka bertipe integer?",
                options = listOf("3.5", "3", "4", "3.0"),
                correctIndex = 1,
                explanation = "Karena operan berupa integer literal, C++ melakukan integer division yang membuang bagian pecahan, menghasilkan 3."
            )
        ),

        Lesson(
            id = "d4_conditionals",
            level = CourseLevel.DASAR,
            order = 4,
            title = "Percabangan Kondisional",
            subtitle = "Pengambilan keputusan dengan if, else if, else, dan switch-case",
            estimatedMinutes = 10,
            summary = "Pelajari bagaimana mengarahkan alur program menggunakan kondisional dan best practices penulisan clean branching.",
            sections = listOf(
                LessonSection.Heading("Struktur if - else if - else"),
                LessonSection.Paragraph(
                    "Struktur kondisional memungkinkan program memilih cabang kode yang akan dijalankan berdasarkan evaluasi kondisi boolean."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    int nilai = 85;

    if (nilai >= 90) {
        std::cout << "Grade: A (Sangat Memuaskan)\n";
    } else if (nilai >= 80) {
        std::cout << "Grade: B (Memuaskan)\n";
    } else if (nilai >= 70) {
        std::cout << "Grade: C (Cukup)\n";
    } else {
        std::cout << "Grade: D (Perlu Peningkatan)\n";
    }
    return 0;
}""",
                    output = "Grade: B (Memuaskan)",
                    explanation = "Kondisi dievaluasi dari atas ke bawah. Begitu satu kondisi bernilai true, blok kodenya dieksekusi dan percabangan selesai."
                ),
                LessonSection.Heading("Pernyataan switch - case"),
                LessonSection.Paragraph(
                    "switch-case sangat efisien ketika Anda memiliki variabel integral/karakter/enum yang dibandingkan dengan banyak nilai konstan."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    char pilihan = 'B';

    switch (pilihan) {
        case 'A':
            std::cout << "Mode: Pemula\n";
            break;
        case 'B':
            std::cout << "Mode: Menengah\n";
            break;
        case 'C':
            std::cout << "Mode: Mahir\n";
            break;
        default:
            std::cout << "Pilihan tidak valid\n";
            break;
    }
    return 0;
}""",
                    output = "Mode: Menengah",
                    explanation = "Jangan lupakan kata kunci 'break'! Jika diabaikan, program akan melakukan 'fall-through' dan mengeksekusi case di bawahnya."
                ),
                LessonSection.TipBox(
                    title = "If with Initializer (C++17)",
                    message = "Di C++17, Anda bisa mendeklarasikan variabel langsung di dalam 'if': if (int code = getStatus(); code == 200) { ... }. Variabel tersebut otomatis musnah setelah blok if selesai."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_d4",
                category = "Kondisional",
                level = CourseLevel.DASAR,
                question = "Apa yang terjadi jika kita tidak menyertakan kata kunci 'break' pada sebuah case di switch statement?",
                options = listOf(
                    "Program menghasilkan error kompilasi",
                    "Program langsung berhenti dan keluar dari main()",
                    "Terjadi fall-through (case berikutnya ikut dieksekusi)",
                    "Switch otomatis diulang kembali dari awal"
                ),
                correctIndex = 2,
                explanation = "Tanpa 'break', aliran eksekusi akan terus meluncur ke case di bawahnya (dikenal sebagai fall-through)."
            )
        ),

        Lesson(
            id = "d5_loops",
            level = CourseLevel.DASAR,
            order = 5,
            title = "Perulangan & Iterasi",
            subtitle = "for, while, do-while, range-based for loop, dan kontrol iterasi",
            estimatedMinutes = 12,
            summary = "Gunakan perulangan untuk mengeksekusi blok kode berulang kali secara efisien dan aman dari infinite loop.",
            sections = listOf(
                LessonSection.Heading("Jenis-Jenis Loop"),
                LessonSection.Paragraph(
                    "C++ menyediakan 4 bentuk perulangan utama: for loop tradisional, while loop, do-while loop, dan range-based for loop (modern C++)."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    // 1. For loop tradisional
    std::cout << "For loop: ";
    for (int i = 1; i <= 5; ++i) {
        std::cout << i << " ";
    }
    std::cout << "\n";

    // 2. While loop (cek kondisi di awal)
    int count = 3;
    std::cout << "Countdown: ";
    while (count > 0) {
        std::cout << count << " ";
        count--;
    }
    std::cout << "Boom!\n";

    // 3. Do-while loop (selalu eksekusi minimal 1 kali)
    int x = 10;
    do {
        std::cout << "Nilai x: " << x << "\n";
        x++;
    } while (x < 10); // Kondisi false, tapi blok di atas sudah jalan 1x

    return 0;
}""",
                    output = "For loop: 1 2 3 4 5 \nCountdown: 3 2 1 Boom!\nNilai x: 10",
                    explanation = "Gunakan 'for' jika jumlah perulangan sudah diketahui pasti, dan 'while' jika perulangan bergantung pada kondisi dinamis."
                ),
                LessonSection.Heading("Range-Based For Loop (C++11)"),
                LessonSection.Paragraph(
                    "Cara paling modern, bersih, dan aman untuk mengiterasi elemen koleksi seperti array atau vector."
                ),
                LessonSection.CodeBlock(
                    code = """int angka[] = {10, 20, 30, 40, 50};

for (int num : angka) {
    std::cout << num * 2 << " ";
}
// Output: 20 40 60 80 100""",
                    output = "20 40 60 80 100",
                    explanation = "Untuk menghindari copy data saat elemen berukuran besar, gunakan referensi: for (const auto& item : koleksi)."
                ),
                LessonSection.Heading("break dan continue"),
                LessonSection.Paragraph(
                    "'break' menghentikan loop sepenuhnya seketika, sedangkan 'continue' melompati sisa blok di iterasi saat ini dan langsung melompat ke iterasi berikutnya."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_d5",
                category = "Perulangan",
                level = CourseLevel.DASAR,
                question = "Struktur perulangan manakah yang dijamin mengeksekusi tubuh kodenya minimal satu kali?",
                options = listOf("for loop", "while loop", "do-while loop", "range-based for loop"),
                correctIndex = 2,
                explanation = "do-while mengevaluasi kondisinya di akhir blok (post-test), sehingga kode di dalam tubuh loop selalu berjalan minimal 1 kali."
            )
        ),

        Lesson(
            id = "d6_arrays_vectors",
            level = CourseLevel.DASAR,
            order = 6,
            title = "Array & std::vector",
            subtitle = "Array statis C-style vs kontainer dinamis std::vector dari STL",
            estimatedMinutes = 12,
            summary = "Memahami perbedaan ukuran tetap pada array statis dan fleksibilitas dinamis dari std::vector.",
            sections = listOf(
                LessonSection.Heading("Array Statis"),
                LessonSection.Paragraph(
                    "Array statis menyimpan sejumlah elemen dengan tipe yang sama dalam blok memori berurutan (kontigu). Ukuran array statis harus konstan dan tidak bisa diubah saat program berjalan."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    int prima[5] = {2, 3, 5, 7, 11};

    // Mengakses elemen dengan indeks 0-based
    std::cout << "Elemen pertama: " << prima[0] << "\n";
    std::cout << "Elemen kelima: " << prima[4] << "\n";

    // Mengubah nilai
    prima[0] = 99;
    std::cout << "Elemen pertama baru: " << prima[0] << "\n";
    return 0;
}""",
                    output = "Elemen pertama: 2\nElemen kelima: 11\nElemen pertama baru: 99",
                    explanation = "Indeks array selalu dimulai dari 0 sampai n-1. Mengakses di luar batas (out of bounds) menyebabkan Undefined Behavior!"
                ),
                LessonSection.Heading("std::vector (Dinamis & Fleksibel)"),
                LessonSection.Paragraph(
                    "std::vector adalah array dinamis yang dapat menambah dan mengurangi ukurannya secara otomatis saat runtime. Ini adalah kontainer paling sering digunakan dalam C++ modern."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <vector>

int main() {
    std::vector<int> nilai;

    // Menambah elemen di akhir
    nilai.push_back(85);
    nilai.push_back(92);
    nilai.push_back(78);

    std::cout << "Jumlah elemen: " << nilai.size() << "\n";

    // Akses dengan pengecekan batas yang aman (.at)
    std::cout << "Nilai ke-2: " << nilai.at(1) << "\n";

    // Menghapus elemen terakhir
    nilai.pop_back();
    std::cout << "Ukuran setelah pop: " << nilai.size() << "\n";
    return 0;
}""",
                    output = "Jumlah elemen: 3\nNilai ke-2: 92\nUkuran setelah pop: 2",
                    explanation = "Metode .at(index) melempar std::out_of_range exception jika indeks tidak valid, membuatnya lebih aman daripada operator []."
                ),
                LessonSection.TipBox(
                    title = "Optimasi .reserve() pada Vector",
                    message = "Jika Anda tahu perkiraan jumlah elemen, panggil 'vector.reserve(N)' sebelum push_back untuk mencegah realokasi memori berulang kali."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_d6",
                category = "Kontainer",
                level = CourseLevel.DASAR,
                question = "Fungsi bawaan std::vector manakah yang digunakan untuk menambahkan elemen baru di urutan paling belakang?",
                options = listOf("append()", "push_back()", "insert_end()", "add()"),
                correctIndex = 1,
                explanation = "std::vector::push_back(value) menambahkan elemen di akhir kontainer dengan kompleksitas rata-rata O(1)."
            )
        ),

        // ================= MENENGAH =================
        Lesson(
            id = "m1_functions",
            level = CourseLevel.MENENGAH,
            order = 7,
            title = "Fungsi & Manajemen Parameter",
            subtitle = "Deklarasi, Pass by Value, Pass by Reference (&), dan Overloading",
            estimatedMinutes = 12,
            summary = "Pahami cara membagi kode menjadi fungsi modular, mekanisme pengiriman argumen, dan fungsi overloading.",
            sections = listOf(
                LessonSection.Heading("Anatomi Fungsi"),
                LessonSection.Paragraph(
                    "Fungsi adalah blok kode terorganisir yang melakukan tugas tertentu dan dapat dipanggil berulang kali. Fungsi memiliki tipe return, nama, dan parameter."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

// Prototipe / Deklarasi fungsi
int tambah(int a, int b);

int main() {
    int hasil = tambah(15, 25);
    std::cout << "Hasil penjumlahan: " << hasil << "\n";
    return 0;
}

// Definisi fungsi
int tambah(int a, int b) {
    return a + b;
}""",
                    output = "Hasil penjumlahan: 40",
                    explanation = "Prototipe fungsi memberi tahu kompilator tentang signature fungsi sebelum fungsi dipanggil di main()."
                ),
                LessonSection.Heading("Pass by Value vs Pass by Reference"),
                LessonSection.Paragraph(
                    "Secara default, argumen dikirim secara 'Pass by Value' (disalin). Gunakan simbol ampersand (&) untuk 'Pass by Reference' agar fungsi dapat mengubah variabel asli tanpa membuat salinan memori baru."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

void tukar(int& x, int& y) {
    int temp = x;
    x = y;
    y = temp;
}

int main() {
    int p = 100, q = 200;
    std::cout << "Sebelum: p=" << p << ", q=" << q << "\n";
    tukar(p, q);
    std::cout << "Sesudah: p=" << p << ", q=" << q << "\n";
    return 0;
}""",
                    output = "Sebelum: p=100, q=200\nSesudah: p=200, q=100",
                    explanation = "Parameter int& x dan int& y adalah alias langsung ke variabel p dan q di memori."
                ),
                LessonSection.TipBox(
                    title = "Pass by const Reference untuk Objek Berat",
                    message = "Untuk parameter objek besar seperti std::string atau std::vector, gunakan 'const T& param'. Ini menghindari duplikasi memori dan menjamin objek tidak diubah di dalam fungsi."
                ),
                LessonSection.Heading("Function Overloading"),
                LessonSection.Paragraph(
                    "C++ mendukung banyak fungsi dengan nama yang sama, asalkan tipe atau jumlah parameternya berbeda (berbeda signature)."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_m1",
                category = "Fungsi",
                level = CourseLevel.MENENGAH,
                question = "Simbol apa yang digunakan pada tipe parameter fungsi untuk menandakan pass-by-reference?",
                options = listOf("*", "&", "@", "->"),
                correctIndex = 1,
                explanation = "Tanda '&' setelah tipe data (contoh: int& x) menandakan bahwa parameter tersebut adalah referensi."
            )
        ),

        Lesson(
            id = "m2_pointers",
            level = CourseLevel.MENENGAH,
            order = 8,
            title = "Pointer & Referensi Alamat Memori",
            subtitle = "Operator &, operator dereference *, pointer aritmatika, dan nullptr",
            estimatedMinutes = 15,
            summary = "Pointer adalah salah satu fitur paling bertenaga di C++. Pahami cara menyimpan dan memanipulasi alamat fisik memori.",
            sections = listOf(
                LessonSection.Heading("Apa Itu Pointer?"),
                LessonSection.Paragraph(
                    "Pointer adalah variabel yang menyimpan alamat memori dari variabel lain. Jika variabel biasa menyimpan data langsung, pointer menyimpan lokasi tempat data tersebut disimpan di RAM."
                ),
                LessonSection.KeyPoints(
                    listOf(
                        "Operator Alamat (&): mengambil alamat memori suatu variabel",
                        "Operator Dereference (*): mengakses nilai yang berada di alamat yang ditunjuk oleh pointer",
                        "nullptr: nilai penunjuk kosong yang aman (menggantikan NULL dan 0 di C++11)"
                    )
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    int nilai = 42;
    int* ptr = &nilai; // ptr menyimpan alamat variabel nilai

    std::cout << "Nilai asli: " << nilai << "\n";
    std::cout << "Alamat memori (&nilai): " << ptr << "\n";
    std::cout << "Nilai via pointer (*ptr): " << *ptr << "\n";

    // Mengubah nilai asli melalui pointer
    *ptr = 99;
    std::cout << "Nilai baru setelah diubah: " << nilai << "\n";
    return 0;
}""",
                    output = "Nilai asli: 42\nAlamat memori (&nilai): 0x7ffd9a8b12\nNilai via pointer (*ptr): 42\nNilai baru setelah diubah: 99",
                    explanation = "*ptr = 99 menulis angka 99 langsung ke alamat memori variabel 'nilai'."
                ),
                LessonSection.WarningBox(
                    title = "Waspadai Dangling & Wild Pointer!",
                    message = "Jangan pernah melakukan dereference pada pointer yang belum diinisialisasi atau pointer yang menunjuk memori yang sudah dibebaskan. Selalu inisialisasi pointer dengan nullptr jika belum memiliki target!"
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_m2",
                category = "Pointer",
                level = CourseLevel.MENENGAH,
                question = "Jika ptr adalah pointer ke integer (int* ptr = &x;), operator apa yang digunakan untuk membaca atau memodifikasi nilai x lewat ptr?",
                options = listOf("&ptr", "*ptr", "->ptr", "ptr.value"),
                correctIndex = 1,
                explanation = "Operator asterisk '*' di depan nama pointer (dereferencing) digunakan untuk mengakses nilai yang ditunjuk."
            )
        ),

        Lesson(
            id = "m3_dynamic_memory",
            level = CourseLevel.MENENGAH,
            order = 9,
            title = "Alokasi Memori Dinamis (Heap vs Stack)",
            subtitle = "Operator new, delete, delete[], pencegahan memory leak",
            estimatedMinutes = 14,
            summary = "Pahami perbedaan memori Stack dan Heap serta tanggung jawab manajemen alokasi manual di C++.",
            sections = listOf(
                LessonSection.Heading("Stack vs Heap"),
                LessonSection.Paragraph(
                    "Stack adalah area memori yang dikelola otomatis oleh sistem (sangat cepat, namun ukurannya terbatas). Variabel lokal hidup di stack. Heap adalah ruang memori bebas yang luas di mana pengembang dapat meminta memori saat runtime menggunakan keyword 'new'."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

int main() {
    // Alokasi single integer di Heap
    int* pAngka = new int(100);
    std::cout << "Nilai di Heap: " << *pAngka << "\n";

    // Wajib dibebaskan setelah selesai!
    delete pAngka;
    pAngka = nullptr; // Mencegah dangling pointer

    // Alokasi array dinamis di Heap
    int ukuran = 3;
    int* pArray = new int[ukuran];
    pArray[0] = 10;
    pArray[1] = 20;
    pArray[2] = 30;

    std::cout << "Elemen array kedua: " << pArray[1] << "\n";

    // Array di heap wajib dihapus dengan delete[]
    delete[] pArray;
    pArray = nullptr;

    return 0;
}""",
                    output = "Nilai di Heap: 100\nElemen array kedua: 20",
                    explanation = "Pasangan alokasi: new berpasangan dengan delete, sedangkan new[] berpasangan dengan delete[]."
                ),
                LessonSection.WarningBox(
                    title = "Bahaya Memory Leak",
                    message = "Jika memori yang dialokasikan dengan 'new' lupa dibebaskan dengan 'delete', memori tersebut akan terus tersita sampai proses dimatikan. Di server yang berjalan berbulan-bulan, memory leak menyebabkan aplikasi crash karena kehabisan RAM."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_m3",
                category = "Memori Dinamis",
                level = CourseLevel.MENENGAH,
                question = "Operator mana yang wajib digunakan untuk membebaskan memori array yang dialokasikan dengan 'new int[10]'?",
                options = listOf("free()", "delete", "delete[]", "remove[]"),
                correctIndex = 2,
                explanation = "Alokasi array dinamis dengan 'new[]' wajib dibersihkan menggunakan operator 'delete[]' agar destruktor setiap elemen terpanggil."
            )
        ),

        Lesson(
            id = "m4_structs_enums",
            level = CourseLevel.MENENGAH,
            order = 10,
            title = "Struct & Scoped Enums (enum class)",
            subtitle = "Mengelompokkan data heterogen dan enumerasi type-safe C++11",
            estimatedMinutes = 10,
            summary = "Gunakan struct untuk struktur data gabungan dan enum class untuk daftar opsi yang aman dari polusi scope.",
            sections = listOf(
                LessonSection.Heading("Struct di C++"),
                LessonSection.Paragraph(
                    "Di C++, struct bukan sekadar penampung data seperti di C murni. Di C++, struct bisa memiliki member method, constructor, dan access specifiers. Perbedaan utama struct dan class di C++ hanyalah default visibility: member struct berstatus public secara default."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <string>

struct Buku {
    std::string judul;
    std::string penulis;
    int tahun;

    void tampilkanInfo() {
        std::cout << "\"" << judul << "\" oleh " << penulis 
                  << " (" << tahun << ")\n";
    }
};

int main() {
    Buku b1 = {"Belajar C++ Modern", "Bjarne", 2024};
    b1.tampilkanInfo();
    return 0;
}""",
                    output = "\"Belajar C++ Modern\" oleh Bjarne (2024)",
                    explanation = "Member struct otomatis public, sehingga dapat langsung diakses b1.judul."
                ),
                LessonSection.Heading("Scoped Enums: enum class (C++11)"),
                LessonSection.Paragraph(
                    "Hindari 'enum' kuno yang mencemari global namespace. Gunakan 'enum class' yang type-safe dan mengharuskan pemanggilan dengan scope prefix."
                ),
                LessonSection.CodeBlock(
                    code = """enum class StatusServer {
    Online,
    Offline,
    Maintenance
};

StatusServer s = StatusServer::Online;
if (s == StatusServer::Online) {
    std::cout << "Server siap menerima koneksi!\n";
}""",
                    output = "Server siap menerima koneksi!",
                    explanation = "enum class tidak bisa dikonversi secara implisit ke integer sembarangan, mencegah bug logika yang sulit dilacak."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_m4",
                category = "Struct & Enum",
                level = CourseLevel.MENENGAH,
                question = "Secara default di C++, apa access modifier untuk anggota sebuah struct jika tidak ditentukan secara eksplisit?",
                options = listOf("private", "protected", "public", "internal"),
                correctIndex = 2,
                explanation = "Semua member struct di C++ berstatus public secara default, berbeda dengan class yang default-nya adalah private."
            )
        ),

        Lesson(
            id = "m5_oop_basics",
            level = CourseLevel.MENENGAH,
            order = 11,
            title = "Dasar Pemrograman Berorientasi Objek (OOP)",
            subtitle = "Class, Object, Constructor, Destructor, dan Member Initializer List",
            estimatedMinutes = 15,
            summary = "Masuk ke jantung OOP di C++: memahami enkapsulasi data, instansiasi objek, konstruktor, dan destruktor.",
            sections = listOf(
                LessonSection.Heading("Class dan Object"),
                LessonSection.Paragraph(
                    "Class adalah blueprint (cetak biru) yang mendefinisikan atribut data dan perilaku (metode). Object adalah wujud nyata (instansi) dari blueprint tersebut di memori."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <string>

class Mobil {
private:
    std::string merk;
    int kecepatan;

public:
    // Konstruktor dengan Member Initializer List
    Mobil(std::string m, int k) : merk(m), kecepatan(k) {
        std::cout << "[Konstruktor dipanggil untuk " << merk << "]\n";
    }

    // Destruktor (dipanggil saat objek keluar dari scope/musnah)
    ~Mobil() {
        std::cout << "[Destruktor dipanggil untuk " << merk << "]\n";
    }

    void gas(int akselerasi) {
        kecepatan += akselerasi;
        std::cout << merk << " melaju dengan kecepatan: " << kecepatan << " km/jam\n";
    }
};

int main() {
    {
        Mobil avanza("Toyota Avanza", 60);
        avanza.gas(20);
    } // Di titik kurung kurawal tutup ini, avanza musnah dan destruktor berjalan!
    std::cout << "Keluar dari scope lokal.\n";
    return 0;
}""",
                    output = "[Konstruktor dipanggil untuk Toyota Avanza]\nToyota Avanza melaju dengan kecepatan: 80 km/jam\n[Destruktor dipanggil untuk Toyota Avanza]\nKeluar dari scope lokal.",
                    explanation = "Member Initializer List (: merk(m), kecepatan(k)) menginisialisasi atribut langsung sebelum tubuh konstruktor berjalan."
                ),
                LessonSection.TipBox(
                    title = "Destruktor & Pembersihan Sumber Daya (RAII)",
                    message = "Destruktor (~NamaClass) adalah senjata rahasia C++. Anda bisa menutup file, memutus koneksi socket, atau membebaskan memori di dalam destruktor secara otomatis!"
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_m5",
                category = "OOP",
                level = CourseLevel.MENENGAH,
                question = "Kapan destruktor (~NamaClass) suatu objek otomatis dijalankan?",
                options = listOf(
                    "Hanya saat program memanggil delete secara manual",
                    "Saat objek keluar dari cakupan (scope) hidupnya",
                    "Hanya saat komputer dimatikan",
                    "Saat fungsi main() pertama kali dipanggil"
                ),
                correctIndex = 1,
                explanation = "Di C++, objek lokal pada stack otomatis menjalankan destruktornya saat keluar dari scope kurung kurawal penutup {}."
            )
        ),

        Lesson(
            id = "m6_encapsulation",
            level = CourseLevel.MENENGAH,
            order = 12,
            title = "Enkapsulasi & Abstraksi",
            subtitle = "Prinsip data hiding, getter/setter, dan pemisahan interface",
            estimatedMinutes = 12,
            summary = "Lindungi integritas data dengan membatasi akses langsung dan memvalidasi perubahan state objek.",
            sections = listOf(
                LessonSection.Heading("Mengapa Butuh Enkapsulasi?"),
                LessonSection.Paragraph(
                    "Enkapsulasi adalah teknik menyatukan data dan fungsi yang memanipulasinya ke dalam satu unit, serta menyembunyikan detail implementasi internal dari dunia luar (data hiding). Ini mencegah variabel objek diisi nilai yang tidak valid (misal: saldo negatif atau umur minus)."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

class RekeningBank {
private:
    double saldo; // Private: tidak bisa diakses dari luar langsung

public:
    RekeningBank(double saldoAwal) {
        saldo = (saldoAwal > 0) ? saldoAwal : 0.0;
    }

    // Getter
    double getSaldo() const {
        return saldo;
    }

    // Setter dengan validasi aturan bisnis
    bool setor(double jumlah) {
        if (jumlah > 0) {
            saldo += jumlah;
            return true;
        }
        return false;
    }

    bool tarik(double jumlah) {
        if (jumlah > 0 && jumlah <= saldo) {
            saldo -= jumlah;
            return true;
        }
        return false;
    }
};

int main() {
    RekeningBank rek(500000);
    rek.setor(250000);
    rek.tarik(100000);

    std::cout << "Sisa saldo akhir: Rp " << rek.getSaldo() << "\n";
    return 0;
}""",
                    output = "Sisa saldo akhir: Rp 650000",
                    explanation = "Keyword 'const' pada getSaldo() const menandakan fungsi ini tidak akan memodifikasi state internal objek (const-correctness)."
                ),
                LessonSection.KeyPoints(
                    listOf(
                        "Gunakan private untuk variabel anggota class",
                        "Sediakan getter dan setter publik hanya bila diperlukan",
                        "Gunakan 'const' pada member function yang hanya membaca data"
                    )
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_m6",
                category = "Enkapsulasi",
                level = CourseLevel.MENENGAH,
                question = "Apa arti kata kunci 'const' di akhir signature metode: 'int getNilai() const;'?",
                options = listOf(
                    "Metode tersebut mengembalikan nilai konstanta yang tidak bisa diubah",
                    "Metode tersebut dijamin tidak akan mengubah variabel anggota class",
                    "Metode tersebut hanya bisa dipanggil satu kali",
                    "Metode tersebut berjalan pada waktu kompilasi"
                ),
                correctIndex = 1,
                explanation = "Konstanta member function (const-qualified) menjamin bahwa pemanggilan metode tersebut bersifat read-only terhadap objek."
            )
        ),

        // ================= MAHIR =================
        Lesson(
            id = "h1_inheritance",
            level = CourseLevel.MAHIR,
            order = 13,
            title = "Pewarisan (Inheritance)",
            subtitle = "Single, Multilevel, Multiple Inheritance, dan access specifier protected",
            estimatedMinutes = 14,
            summary = "Terapkan reusabilitas kode dengan mewariskan atribut dan metode dari parent class ke child class.",
            sections = listOf(
                LessonSection.Heading("Konsep Pewarisan"),
                LessonSection.Paragraph(
                    "Pewarisan memungkinkan class baru (derived/child class) mengadopsi sifat dari class yang sudah ada (base/parent class). Ini mencerminkan hubungan 'is-a' (adalah sebuah)."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <string>

// Base Class
class Hewan {
protected:
    std::string nama; // Bisa diakses oleh class turunan

public:
    Hewan(std::string n) : nama(n) {}

    void makan() {
        std::cout << nama << " sedang makan.\n";
    }
};

// Derived Class
class Kucing : public Hewan {
public:
    Kucing(std::string n) : Hewan(n) {} // Memanggil konstruktor parent

    void bersuara() {
        std::cout << nama << ": Meong meong!\n";
    }
};

int main() {
    Kucing k("Mochi");
    k.makan();     // Method warisan dari Hewan
    k.bersuara();  // Method spesifik Kucing
    return 0;
}""",
                    output = "Mochi sedang makan.\nMochi: Meong meong!",
                    explanation = "'public Hewan' berarti anggota public parent tetap public di child, dan protected tetap protected."
                ),
                LessonSection.TipBox(
                    title = "Multiple Inheritance di C++",
                    message = "Berbeda dengan Java atau C#, C++ mendukung Multiple Inheritance (satu class mewarisi lebih dari satu parent). Hati-hati dengan Diamond Problem yang dapat diselesaikan dengan 'virtual inheritance'!"
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_h1",
                category = "Inheritance",
                level = CourseLevel.MAHIR,
                question = "Access specifier manakah yang memperbolehkan data diakses oleh class turunan (derived class) namun tetap tersembunyi dari luar?",
                options = listOf("private", "protected", "public", "internal"),
                correctIndex = 1,
                explanation = "'protected' memungkinkan akses oleh class turunan tanpa mengeksposnya ke publik luas."
            )
        ),

        Lesson(
            id = "h2_polymorphism",
            level = CourseLevel.MAHIR,
            order = 14,
            title = "Polimorfisme & Virtual Functions",
            subtitle = "virtual, override, pure virtual functions, Abstract Class, dan VTable",
            estimatedMinutes = 16,
            summary = "Kuasai dynamic dispatch saat runtime menggunakan fungsi virtual dan kelas abstrak.",
            sections = listOf(
                LessonSection.Heading("Runtime Polymorphism & keyword virtual"),
                LessonSection.Paragraph(
                    "Tanpa keyword 'virtual', C++ menggunakan early-binding (menentukan fungsi mana yang dipanggil berdasarkan tipe pointer saat kompilasi). Dengan 'virtual', C++ menggunakan dynamic dispatch melalui Virtual Table (VTable) sehingga fungsi yang dipanggil sesuai objek aslinya."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <vector>

// Abstract Base Class
class BangunDatar {
public:
    // Pure virtual function (= 0)
    virtual double hitungLuas() const = 0;

    // Virtual destructor sangat penting pada polimorfisme!
    virtual ~BangunDatar() = default;
};

class Persegi : public BangunDatar {
private:
    double sisi;
public:
    Persegi(double s) : sisi(s) {}
    double hitungLuas() const override {
        return sisi * sisi;
    }
};

class Lingkaran : public BangunDatar {
private:
    double radius;
public:
    Lingkaran(double r) : radius(r) {}
    double hitungLuas() const override {
        return 3.14159 * radius * radius;
    }
};

int main() {
    Persegi p(4.0);
    Lingkaran l(7.0);

    std::vector<BangunDatar*> daftar = {&p, &l};
    for (const auto* b : daftar) {
        std::cout << "Luas: " << b->hitungLuas() << "\n";
    }
    return 0;
}""",
                    output = "Luas: 16\nLuas: 153.938",
                    explanation = "Override memastikan nama fungsi dan parameternya tepat sama dengan fungsi virtual di parent class."
                ),
                LessonSection.WarningBox(
                    title = "Selalu Buat Virtual Destructor di Base Class!",
                    message = "Jika Anda menghapus pointer child class melalui pointer base class (delete basePtr), tanpa virtual destructor destruktor child tidak akan dipanggil, berujung kebocoran memori."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_h2",
                category = "Polimorfisme",
                level = CourseLevel.MAHIR,
                question = "Suatu class di C++ otomatis menjadi Abstract Class apabila memiliki minimal satu:",
                options = listOf(
                    "Virtual function biasa",
                    "Pure virtual function (= 0)",
                    "Private constructor",
                    "Static member function"
                ),
                correctIndex = 1,
                explanation = "Sebuah class yang memiliki minimal satu fungsi pure virtual (virtual void foo() = 0;) menjadi kelas abstrak yang tidak bisa diinstansiasi langsung."
            )
        ),

        Lesson(
            id = "h3_operator_overloading",
            level = CourseLevel.MAHIR,
            order = 15,
            title = "Operator Overloading",
            subtitle = "Mendefinisikan ulang operator +, ==, dan << (cout) untuk tipe kustom",
            estimatedMinutes = 14,
            summary = "Berikan makna intuitif pada operator standar ketika bekerja dengan objek buatan sendiri.",
            sections = listOf(
                LessonSection.Heading("Mengapa Overload Operator?"),
                LessonSection.Paragraph(
                    "Operator overloading memungkinkan objek buatan sendiri digunakan dengan operator alami seperti +, -, *, ==, atau <<. Contohnya, menjumlahkan dua titik koordinat: Titik c = a + b."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>

class Vektor2D {
public:
    double x, y;

    Vektor2D(double x_val = 0, double y_val = 0) : x(x_val), y(y_val) {}

    // Overload operator +
    Vektor2D operator+(const Vektor2D& other) const {
        return Vektor2D(x + other.x, y + other.y);
    }

    // Overload operator ==
    bool operator==(const Vektor2D& other) const {
        return (x == other.x && y == other.y);
    }

    // Overload operator << (stream insertion) sebagai fungsi friend
    friend std::ostream& operator<<(std::ostream& os, const Vektor2D& v) {
        os << "(" << v.x << ", " << v.y << ")";
        return os;
    }
};

int main() {
    Vektor2D v1(3, 4);
    Vektor2D v2(1, 2);
    Vektor2D v3 = v1 + v2; // Memanggil operator+

    std::cout << "v1 + v2 = " << v3 << "\n";
    std::cout << "Apakah v1 == v2? " << (v1 == v2 ? "Ya" : "Tidak") << "\n";
    return 0;
}""",
                    output = "v1 + v2 = (4, 6)\nApakah v1 == v2? Tidak",
                    explanation = "Fungsi friend untuk operator<< memungkinkan std::cout << obj ditulis dengan sintaks yang wajar."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_h3",
                category = "Operator Overloading",
                level = CourseLevel.MAHIR,
                question = "Operator manakah dari daftar berikut yang TIDAK BISA di-overload di C++?",
                options = listOf("+", "==", ":: (scope resolution)", "[] (subscript)"),
                correctIndex = 2,
                explanation = "Operator :: (scope resolution), . (member access), .* (pointer-to-member), dan ?: (ternary) tidak dapat di-overload di C++."
            )
        ),

        Lesson(
            id = "h4_templates",
            level = CourseLevel.MAHIR,
            order = 16,
            title = "Templates & Generic Programming",
            subtitle = "Function Templates, Class Templates, dan kompilasi generik",
            estimatedMinutes = 15,
            summary = "Tulis kode sekali, gunakan untuk segala tipe data tanpa mengorbankan performa compile-time.",
            sections = listOf(
                LessonSection.Heading("Template Fungsi"),
                LessonSection.Paragraph(
                    "Templates adalah pondasi dari pemrograman generik di C++. Kompilator secara otomatis membuat instansiasi kode biner untuk setiap tipe data yang digunakan."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <string>

template <typename T>
T cariMaksimum(T a, T b) {
    return (a > b) ? a : b;
}

int main() {
    std::cout << "Maks int: " << cariMaksimum<int>(10, 25) << "\n";
    std::cout << "Maks double: " << cariMaksimum(3.14, 2.71) << "\n"; // Tipe disimpulkan otomatis
    std::cout << "Maks string: " << cariMaksimum(std::string("Apel"), std::string("Jeruk")) << "\n";
    return 0;
}""",
                    output = "Maks int: 25\nMaks double: 3.14\nMaks string: Jeruk",
                    explanation = "Kompilator membuat salinan fungsi terpisah untuk int, double, dan string tanpa overhead runtime."
                ),
                LessonSection.Heading("Template Class"),
                LessonSection.Paragraph(
                    "Class template memungkinkan pembuatan struktur data generik seperti Kotak<T> atau pasangan nilai Pasangan<K, V>."
                ),
                LessonSection.CodeBlock(
                    code = """template <typename T>
class Kotak {
private:
    T isi;
public:
    Kotak(T i) : isi(i) {}
    T ambil() const { return isi; }
};

Kotak<int> k1(500);
Kotak<std::string> k2("C++ Template");""",
                    output = null,
                    explanation = "std::vector<T> yang sering Anda gunakan sebenarnya adalah class template dari STL!"
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_h4",
                category = "Templates",
                level = CourseLevel.MAHIR,
                question = "Kapan kode biner dari sebuah template di C++ sebenarnya digenerate oleh sistem?",
                options = listOf(
                    "Saat waktu eksekusi (runtime) lewat JIT",
                    "Saat waktu kompilasi (compile-time) saat template diinstansiasi",
                    "Hanya saat class dimuat ke dalam cache",
                    "Sebelum kode ditulis"
                ),
                correctIndex = 1,
                explanation = "C++ template bersifat compile-time polymorhism. Kompilator men-generate implementasi konkret untuk setiap tipe yang digunakan saat kompilasi."
            )
        ),

        Lesson(
            id = "h5_smart_pointers",
            level = CourseLevel.MAHIR,
            order = 17,
            title = "Smart Pointers & RAII",
            subtitle = "std::unique_ptr, std::shared_ptr, std::weak_ptr, dan safe memory",
            estimatedMinutes = 16,
            summary = "Ucapkan selamat tinggal pada manual 'delete' dan memory leaks dengan smart pointers modern C++11.",
            sections = listOf(
                LessonSection.Heading("Filosofi RAII"),
                LessonSection.Paragraph(
                    "RAII (Resource Acquisition Is Initialization) adalah pilar modern C++. Sumber daya (memori heap, file handle, mutex) diikat pada masa hidup objek stack. Saat objek musnah, destruktornya otomatis membebaskan sumber daya tersebut."
                ),
                LessonSection.Heading("1. std::unique_ptr (Kepemilikan Eksklusif)"),
                LessonSection.Paragraph(
                    "Hanya boleh ada satu pemilik untuk objek di heap. Sangat ringan, nol biaya runtime overhead dibanding raw pointer!"
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <memory> // Pustaka smart pointer

class Aset {
public:
    Aset() { std::cout << "Aset diciptakan!\n"; }
    ~Aset() { std::cout << "Aset otomatis dimusnahkan!\n"; }
    void tampil() { std::cout << "Aset aktif digunakan.\n"; }
};

int main() {
    {
        // Alokasi aman dengan std::make_unique (C++14)
        std::unique_ptr<Aset> ptr = std::make_unique<Aset>();
        ptr->tampil();
    } // Di sini ptr musnah, dan memori otomatis di-delete! Tidak perlu panggil delete!
    std::cout << "Selesai scope blok.\n";
    return 0;
}""",
                    output = "Aset diciptakan!\nAset aktif digunakan.\nAset otomatis dimusnahkan!\nSelesai scope blok.",
                    explanation = "std::unique_ptr tidak bisa disalin (copy disabled), namun bisa dipindahkan kepemilikannya menggunakan std::move()."
                ),
                LessonSection.Heading("2. std::shared_ptr & std::weak_ptr"),
                LessonSection.Paragraph(
                    "std::shared_ptr menggunakan reference counting. Memori hanya dihapus ketika semua shared_ptr yang menunjuk ke objek tersebut sudah musnah (count = 0). std::weak_ptr digunakan untuk memecah circular reference (siklus melingkar yang menyebabkan kebocoran)."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_h5",
                category = "Smart Pointers",
                level = CourseLevel.MAHIR,
                question = "Mengapa 'std::unique_ptr' tidak bisa disalin dengan operator penugasan '='?",
                options = listOf(
                    "Karena fiturnya belum selesai diimplementasikan",
                    "Karena menerapkan prinsip kepemilikan eksklusif (hanya boleh ada 1 pemilik)",
                    "Karena membutuhkan pustaka eksternal",
                    "Karena ukurannya terlalu besar untuk disalin"
                ),
                correctIndex = 1,
                explanation = "std::unique_ptr menjamin kepemilikan tunggal (exclusive ownership) sehingga copy-constructor dan copy-assignment dihapus (deleted). Gunakan std::move untuk transfer kepemilikan."
            )
        ),

        Lesson(
            id = "h6_stl_containers",
            level = CourseLevel.MAHIR,
            order = 18,
            title = "Standard Template Library (STL)",
            subtitle = "Map, Set, Unordered Map, Algoritma std::sort, std::find, std::accumulate",
            estimatedMinutes = 15,
            summary = "Manfaatkan kekuatan pustaka bawaan C++ untuk struktur data tingkat lanjut dan algoritma berkecepatan tinggi.",
            sections = listOf(
                LessonSection.Heading("Pilar-Pilar STL"),
                LessonSection.Paragraph(
                    "STL terdiri dari tiga komponen utama: Containers (wadah data), Iterators (penghubung penunjuk elemen), dan Algorithms (fungsi pemrosesan data siap pakai)."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <vector>
#include <algorithm> // Untuk std::sort, std::reverse
#include <map>       // Untuk key-value dictionary

int main() {
    // 1. Algoritma std::sort
    std::vector<int> angka = {45, 12, 89, 3, 27};
    std::sort(angka.begin(), angka.end());

    std::cout << "Urut: ";
    for (int n : angka) std::cout << n << " ";
    std::cout << "\n";

    // 2. std::map (Key-Value, otomatis terurut berdasarkan key)
    std::map<std::string, int> inventaris;
    inventaris["Laptop"] = 15;
    inventaris["Monitor"] = 30;
    inventaris["Mouse"] = 50;

    std::cout << "Stok Monitor: " << inventaris["Monitor"] << " unit\n";
    return 0;
}""",
                    output = "Urut: 3 12 27 45 89 \nStok Monitor: 30 unit",
                    explanation = "std::map diimplementasikan dengan Red-Black Tree (waktu pencarian O(log N)). Untuk O(1) hash table, gunakan std::unordered_map."
                ),
                LessonSection.TipBox(
                    title = "Kapan Memilih std::unordered_map vs std::map?",
                    message = "Pilih std::unordered_map saat Anda membutuhkan performa lookup sangat cepat O(1) dan urutan kunci tidak penting. Pilih std::map saat Anda ingin data selalu berurutan secara alfabetis/numerik."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_h6",
                category = "STL",
                level = CourseLevel.MAHIR,
                question = "Struktur data internal apa yang umumnya digunakan oleh 'std::map' dalam pustaka standar C++?",
                options = listOf("Hash Table", "Self-Balancing Binary Search Tree (Red-Black Tree)", "Array Dinamis", "Singly Linked List"),
                correctIndex = 1,
                explanation = "std::map mengimplementasikan Red-Black Tree yang menjamin kompleksitas pencarian, penyisipan, dan penghapusan bernilai O(log N)."
            )
        ),

        Lesson(
            id = "h7_modern_features",
            level = CourseLevel.MAHIR,
            order = 19,
            title = "Modern C++ (Move Semantics & Lambda)",
            subtitle = "Lambda Expressions [capture], Move Semantics (std::move), dan Exception Handling",
            estimatedMinutes = 16,
            summary = "Ketahui fitur mutakhir yang mengubah cara penulisan kode C++ modern: ekspresi lambda fungsional dan eliminasi deep copy.",
            sections = listOf(
                LessonSection.Heading("Lambda Expressions di C++"),
                LessonSection.Paragraph(
                    "Lambda adalah fungsi anonim inline yang dapat dibuat langsung di tempat pemanggilannya. Sintaks dasar: [capture](parameter) -> return_type { body }."
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <vector>
#include <algorithm>

int main() {
    std::vector<int> data = {1, 2, 3, 4, 5, 6, 7, 8};

    // Menghitung angka genap menggunakan lambda inline
    int jumlahGenap = std::count_if(data.begin(), data.end(), [](int n) {
        return n % 2 == 0;
    });

    std::cout << "Jumlah angka genap: " << jumlahGenap << "\n";
    return 0;
}""",
                    output = "Jumlah angka genap: 4",
                    explanation = "Tanda kurung siku [] adalah capture clause untuk menangkap variabel dari scope luar baik dengan nilai [=] atau referensi [&]."
                ),
                LessonSection.Heading("Move Semantics & std::move"),
                LessonSection.Paragraph(
                    "Sebelum C++11, memindahkan data besar dari objek sementara selalu melibatkan 'deep copy' yang boros. Move Semantics memungkinkan objek baru 'mencuri' pointer data dari objek sementara tanpa menyalin memori fisik sama sekali!"
                ),
                LessonSection.CodeBlock(
                    code = """#include <iostream>
#include <vector>
#include <utility> // std::move

int main() {
    std::vector<int> v1(1000000, 42); // 1 juta elemen
    // Move: hanya memindahkan 3 pointer internal, instan O(1)!
    std::vector<int> v2 = std::move(v1);

    std::cout << "Ukuran v2 sekarang: " << v2.size() << "\n";
    std::cout << "Ukuran v1 setelah move: " << v1.size() << " (kosong!)\n";
    return 0;
}""",
                    output = "Ukuran v2 sekarang: 1000000\nUkuran v1 setelah move: 0 (kosong!)",
                    explanation = "std::move mengubah lvalue menjadi rvalue reference (&&) sehingga konstruktor perpindahan (Move Constructor) terpanggil."
                )
            ),
            quickCheck = QuizQuestion(
                id = "qc_h7",
                category = "Modern C++",
                level = CourseLevel.MAHIR,
                question = "Apa fungsi utama dari 'std::move' di modern C++?",
                options = listOf(
                    "Memindahkan file di sistem operasi",
                    "Mengubah objek menjadi rvalue reference sehingga Move Semantics aktif",
                    "Menghapus objek dari memori seketika",
                    "Memindahkan thread ke core CPU lain"
                ),
                correctIndex = 1,
                explanation = "std::move melakukan static_cast ke rvalue reference (T&&) sehingga resource objek dapat dipindahkan alih-alih diduplikasi secara mendalam."
            )
        )
    )
}
