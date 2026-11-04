package com.example.data

import com.example.data.model.CourseLevel
import com.example.data.model.QuizQuestion

object CppQuizData {
    val allQuestions: List<QuizQuestion> = listOf(
        // DASAR
        QuizQuestion(
            id = "q_d_1",
            category = "Sintaks Dasar",
            level = CourseLevel.DASAR,
            question = "Manakah baris kode yang benar untuk mencetak teks ke layar dan membuat baris baru?",
            codeSnippet = null,
            options = listOf(
                "System.out.println(\"Hello\");",
                "std::cout << \"Hello\" << std::endl;",
                "print(\"Hello\")",
                "echo \"Hello\";"
            ),
            correctIndex = 1,
            explanation = "Di C++, std::cout menggunakan operator insertion << bersama std::endl atau '\\n' untuk output ke stream konsol."
        ),
        QuizQuestion(
            id = "q_d_2",
            category = "Tebak Output",
            level = CourseLevel.DASAR,
            question = "Berapakah nilai yang dicetak oleh kode C++ berikut?",
            codeSnippet = """int a = 15;
int b = 4;
std::cout << a % b;""",
            options = listOf("3", "3.75", "1", "0"),
            correctIndex = 0,
            explanation = "Operator modulo (%) menghitung sisa bagi: 15 dibagi 4 adalah 3 dengan sisa 3."
        ),
        QuizQuestion(
            id = "q_d_3",
            category = "Tipe Data",
            level = CourseLevel.DASAR,
            question = "Berapa byte memori yang biasanya dialokasikan untuk tipe data 'double' pada arsitektur modern 64-bit?",
            codeSnippet = null,
            options = listOf("2 byte", "4 byte", "8 byte", "16 byte"),
            correctIndex = 2,
            explanation = "Tipe data double mengikuti standar IEEE 754 dan menggunakan 8 byte (64 bit) memori."
        ),
        QuizQuestion(
            id = "q_d_4",
            category = "Tebak Output",
            level = CourseLevel.DASAR,
            question = "Apakah output dari program percabangan berikut?",
            codeSnippet = """int x = 10;
if (x > 5 && x < 15) {
    std::cout << "Lolos";
} else {
    std::cout << "Gagal";
}""",
            options = listOf("Lolos", "Gagal", "Error Kompilasi", "Tidak ada output"),
            correctIndex = 0,
            explanation = "Karena 10 > 5 bernilai true DAN 10 < 15 bernilai true, operator && menghasilkan true, mencetak 'Lolos'."
        ),
        QuizQuestion(
            id = "q_d_5",
            category = "Perulangan",
            level = CourseLevel.DASAR,
            question = "Berapa kali kata 'Iterasi' akan dicetak oleh perulangan berikut?",
            codeSnippet = """for (int i = 0; i < 5; i += 2) {
    std::cout << "Iterasi\n";
}""",
            options = listOf("2 kali", "3 kali (saat i = 0, 2, 4)", "5 kali", "4 kali"),
            correctIndex = 1,
            explanation = "Iterasi berjalan saat i=0, lalu i=2, lalu i=4. Pada i=6 kondisi i<5 menjadi false. Total = 3 kali."
        ),
        QuizQuestion(
            id = "q_d_6",
            category = "Array",
            level = CourseLevel.DASAR,
            question = "Jika 'int arr[5] = {1, 2, 3};', berapakah nilai dari elemen arr[4]?",
            codeSnippet = null,
            options = listOf("Undefined / Garbage value", "0", "3", "Error segfault"),
            correctIndex = 1,
            explanation = "Di C++, jika sebuah array diinisialisasi sebagian dengan daftar kurung kurawal, sisa elemen yang tidak ditentukan otomatis diisi nilai default 0 (zero-initialized)."
        ),
        QuizQuestion(
            id = "q_d_7",
            category = "Sintaks C++",
            level = CourseLevel.DASAR,
            question = "Manakah cara mendeklarasikan konstanta integer waktu kompilasi yang benar di modern C++?",
            codeSnippet = null,
            options = listOf(
                "constant int MAX = 100;",
                "constexpr int MAX = 100;",
                "immutable int MAX = 100;",
                "final int MAX = 100;"
            ),
            correctIndex = 1,
            explanation = "Keyword 'constexpr' diperkenalkan di C++11 untuk konstanta murni yang dievaluasi saat compile-time."
        ),
        QuizQuestion(
            id = "q_d_8",
            category = "Tebak Output",
            level = CourseLevel.DASAR,
            question = "Berapakah output dari kode berikut?",
            codeSnippet = """int a = 5;
int b = ++a; // pre-increment
std::cout << a << " " << b;""",
            options = listOf("5 5", "6 5", "6 6", "5 6"),
            correctIndex = 2,
            explanation = "Pre-increment (++a) menambah nilai 'a' menjadi 6 terlebih dahulu sebelum nilainya diberikan ke 'b', sehingga keduanya bernilai 6."
        ),

        // MENENGAH
        QuizQuestion(
            id = "q_m_1",
            category = "Pointer",
            level = CourseLevel.MENENGAH,
            question = "Perhatikan kode pointer berikut. Berapakah nilai variabel 'x' di akhir?",
            codeSnippet = """int x = 20;
int* ptr = &x;
*ptr += 15;
std::cout << x;""",
            options = listOf("20", "35", "Alamat memori x", "Error"),
            correctIndex = 1,
            explanation = "*ptr mendereference alamat x, sehingga *ptr += 15 menambahkan 15 langsung ke memori x, membuat nilai x menjadi 35."
        ),
        QuizQuestion(
            id = "q_m_2",
            category = "Fungsi",
            level = CourseLevel.MENENGAH,
            question = "Apa akibat jika parameter berupa 'void proses(std::vector<int> data)' tanpa tanda referensi (&)?",
            codeSnippet = null,
            options = listOf(
                "Fungsi dapat mengubah isi vector asli",
                "Seluruh elemen vector akan disalin (deep copy) ke memori baru setiap pemanggilan",
                "Kompilator menolak kompilasi",
                "Vector otomatis dihapus"
            ),
            correctIndex = 1,
            explanation = "Pass by value menyalin seluruh data ke stack fungsi baru. Gunakan 'const std::vector<int>&' untuk menghindari overhead salinan."
        ),
        QuizQuestion(
            id = "q_m_3",
            category = "Memori Dinamis",
            level = CourseLevel.MENENGAH,
            question = "Apa nama kondisi ketika memori di Heap yang dialokasikan dengan 'new' tidak pernah dibebaskan dengan 'delete'?",
            codeSnippet = null,
            options = listOf("Stack Overflow", "Memory Leak", "Segmentation Fault", "Deadlock"),
            correctIndex = 1,
            explanation = "Memory leak terjadi saat program mempertahankan memori yang tidak lagi dibutuhkan tanpa mengembalikannya ke sistem operasi."
        ),
        QuizQuestion(
            id = "q_m_4",
            category = "Tebak Output",
            level = CourseLevel.MENENGAH,
            question = "Berapakah output dari program pointer berikut?",
            codeSnippet = """int arr[] = {10, 20, 30, 40};
int* p = arr;
p += 2;
std::cout << *p;""",
            options = listOf("10", "20", "30", "40"),
            correctIndex = 2,
            explanation = "Pointer 'p' awalnya menunjuk arr[0]. Operasi pointer aritmatika 'p += 2' memajukan pointer sejauh 2 elemen ke arr[2] yang bernilai 30."
        ),
        QuizQuestion(
            id = "q_m_5",
            category = "OOP",
            level = CourseLevel.MENENGAH,
            question = "Apa perbedaan utama antara 'class' dan 'struct' di C++?",
            codeSnippet = null,
            options = listOf(
                "struct tidak bisa memiliki metode atau konstruktor",
                "class disimpan di Heap, struct di Stack",
                "Default access level: struct adalah public, sedangkan class adalah private",
                "struct tidak mendukung inheritance"
            ),
            correctIndex = 2,
            explanation = "Di C++, satu-satunya perbedaan teknis antara class dan struct adalah default access specifier (public untuk struct, private untuk class)."
        ),
        QuizQuestion(
            id = "q_m_6",
            category = "OOP",
            level = CourseLevel.MENENGAH,
            question = "Apakah fungsi dari Member Initializer List pada konstruktor C++?",
            codeSnippet = """class Kotak {
    int panjang;
public:
    Kotak(int p) : panjang(p) {}
};""",
            options = listOf(
                "Menginisialisasi member sebelum tubuh konstruktor dieksekusi",
                "Menghapus memori variabel",
                "Mengganti nilai private menjadi public",
                "Menjadikan objek immutable"
            ),
            correctIndex = 0,
            explanation = "Member initializer list secara efisien menginisialisasi atribut langsung di tempatnya, wajib untuk member const dan referensi (&)."
        ),
        QuizQuestion(
            id = "q_m_7",
            category = "Tebak Output",
            level = CourseLevel.MENENGAH,
            question = "Berapakah output dari kode referensi berikut?",
            codeSnippet = """int a = 100;
int& ref = a;
ref = 500;
std::cout << a;""",
            options = listOf("100", "500", "Alamat a", "Undefined"),
            correctIndex = 1,
            explanation = "Referensi 'ref' adalah alias langsung dari variabel 'a'. Mengubah 'ref' sama dengan mengubah 'a' secara langsung."
        ),

        // MAHIR
        QuizQuestion(
            id = "q_h_1",
            category = "Polimorfisme",
            level = CourseLevel.MAHIR,
            question = "Mengapa Base Class yang memiliki fungsi virtual WAJIB memiliki virtual destructor?",
            codeSnippet = """class Base {
public:
    virtual ~Base() = default;
};""",
            options = listOf(
                "Agar kompilator tidak mengubah class menjadi struct",
                "Agar destruktor child class terpanggil saat objek dihapus melalui pointer Base*",
                "Untuk mempercepat waktu eksekusi",
                "Supaya fungsi virtual bisa di-inline"
            ),
            correctIndex = 1,
            explanation = "Jika destruktor base tidak 'virtual', memanggil delete pada Base* yang menunjuk ke Derived* hanya akan memanggil destruktor Base, menyebabkan kebocoran memori child."
        ),
        QuizQuestion(
            id = "q_h_2",
            category = "Smart Pointers",
            level = CourseLevel.MAHIR,
            question = "Bagaimanakah cara mentransfer kepemilikan objek yang dipegang oleh 'std::unique_ptr' ke pointer lain?",
            codeSnippet = null,
            options = listOf(
                "ptr2 = ptr1; (operator penugasan biasa)",
                "ptr2 = std::move(ptr1);",
                "ptr2 = ptr1.clone();",
                "ptr2 = copy(ptr1);"
            ),
            correctIndex = 1,
            explanation = "Karena std::unique_ptr menerapkan kepemilikan tunggal, ia tidak bisa dicopy, melainkan harus dipindahkan menggunakan std::move()."
        ),
        QuizQuestion(
            id = "q_h_3",
            category = "STL",
            level = CourseLevel.MAHIR,
            question = "Berapakah kompleksitas waktu rata-rata (average time complexity) untuk pencarian kunci pada 'std::unordered_map'?",
            codeSnippet = null,
            options = listOf("O(1)", "O(log N)", "O(N)", "O(N log N)"),
            correctIndex = 0,
            explanation = "std::unordered_map mengimplementasikan Hash Table dengan akses rata-rata berkecepatan konstan O(1)."
        ),
        QuizQuestion(
            id = "q_h_4",
            category = "Tebak Output",
            level = CourseLevel.MAHIR,
            question = "Apakah output dari pemanggilan lambda expression berikut?",
            codeSnippet = """int faktor = 3;
auto kali = [faktor](int x) { return x * faktor; };
std::cout << kali(7);""",
            options = listOf("21", "10", "7", "3"),
            correctIndex = 0,
            explanation = "Lambda menangkap variabel 'faktor' (nilai 3) melalui capture clause [faktor] dan mengembalikannya dikali x (7 * 3 = 21)."
        ),
        QuizQuestion(
            id = "q_h_5",
            category = "Move Semantics",
            level = CourseLevel.MAHIR,
            question = "Tipe ekspresi manakah yang ditandai dengan dua ampersand 'T&&' di C++11?",
            codeSnippet = null,
            options = listOf(
                "Pointer ganda",
                "Rvalue Reference (referensi objek sementara)",
                "Operator logika AND ganda",
                "Konstanta referensi global"
            ),
            correctIndex = 1,
            explanation = "T&& adalah sintaks rvalue reference di C++11 yang memungkinkan Move Constructor mendeteksi objek sementara dan mencuri sumber dayanya."
        ),
        QuizQuestion(
            id = "q_h_6",
            category = "Templates",
            level = CourseLevel.MAHIR,
            question = "Apa istilah untuk teknik pembuatan implementasi khusus dari template untuk tipe data tertentu?",
            codeSnippet = null,
            options = listOf(
                "Template Specialization",
                "Template Inheritance",
                "Template Casting",
                "Generic Extension"
            ),
            correctIndex = 0,
            explanation = "Template Specialization memungkinkan pengembang mendefinisikan implementasi kustom jika template digunakan dengan tipe tertentu (misal: template<> class Kotak<char*>)."
        )
    )

    fun getQuestionsByLevel(level: CourseLevel): List<QuizQuestion> {
        return allQuestions.filter { it.level == level }
    }

    fun getRandomQuiz(count: Int = 5): List<QuizQuestion> {
        return allQuestions.shuffled().take(count)
    }
}
