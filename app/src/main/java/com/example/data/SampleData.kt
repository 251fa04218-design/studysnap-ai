package com.example.data

import com.example.model.ChatMessage
import com.example.model.Flashcard
import com.example.model.MaterialType
import com.example.model.QuizQuestion
import com.example.model.StudyMaterial
import org.json.JSONArray

object SampleData {

    val csMaterial = StudyMaterial(
        id = 1L,
        title = "CS 480: Neural Processing Units & Heterogeneous Computing",
        subject = "Computer Science",
        type = MaterialType.PDF,
        sourceFileName = "CS480_Snapdragon_NPU_Architecture.pdf",
        rawContent = """
Lecture Overview:
The transition from cloud-dependent generative AI to on-device AI marks a foundational paradigm shift in consumer computing, prominently seen in Snapdragon X Elite and Snapdragon X Plus platforms powering next-generation HP PCs.

1. The Qualcomm Hexagon NPU Architecture:
Traditional x86 CPUs and mobile GPUs consume substantial wattage (30W to 120W) during continuous matrix multiplication. The Qualcomm Hexagon NPU operates as a dedicated neural accelerator providing up to 45 TOPS (Trillion Operations Per Second) at sub-4W power envelopes. It utilizes specialized INT4, INT8, and FP16 tensor accelerators, extensive micro-tile caches, and vector processing units specifically crafted for Transformer attention mechanisms and depthwise convolutions.

2. Zero-Cloud Privacy & Educational Security:
Students, researchers, and university labs routinely handle sensitive unpublished manuscripts, proprietary codebases, medical datasets, and private lecture audio. Running quantized Large Language Models (LLMs) and Automatic Speech Recognition (ASR) locally on the Hexagon NPU guarantees that zero bytes of student data leave the machine. Air-gapped compliance is maintained by design without relying on remote cloud subscription servers.

3. Quantization & Low-Latency Responsiveness:
By quantizing weights to 4-bit integer representations (INT4), parameter storage is minimized to ~2GB to 4GB of unified LPDDR5x RAM. The NPU streaming pipeline achieves inferencing speeds exceeding 40 tokens per second, allowing instantaneous active recall, zero network lag, and full offline functionality even inside lecture halls or flights without internet.

Key Architecture Equations:
• Peak TOPS = 2 × Frequency (GHz) × Multiply-Accumulate Units (MACs) / 10^3
• Latency (ms) = Time-to-First-Token (TTFT) + (Output Tokens × Inter-Token Latency)
• TOPS/Watt Efficiency: Snapdragon Hexagon NPU achieves ~15 TOPS/W vs ~2 TOPS/W on discrete mobile GPUs.
        """.trimIndent(),
        summary = "Executive Overview:\nExplores on-device neural processing units (NPUs) on Snapdragon HP PCs. Focuses on the Hexagon NPU delivering 45 TOPS at sub-4W power envelopes, enabling air-gapped student privacy and real-time offline AI inferencing.\n\nKey Insights:\n• Dedicated INT4/INT8 tensor accelerators dramatically reduce memory bandwidth and power consumption.\n• Zero cloud upload guarantees 100% intellectual property protection for student notes.\n• Unified memory bandwidth in Snapdragon X Elite ensures sub-20ms first-token latency.",
        keyConceptsJson = JSONArray(listOf(
            "Hexagon NPU (45 TOPS)",
            "Zero-Cloud Air-Gapped Privacy",
            "INT4/INT8 Model Quantization",
            "Heterogeneous Compute (CPU + GPU + NPU)",
            "TOPS/Watt Energy Efficiency"
        )).toString(),
        keyFormulasJson = JSONArray(listOf(
            "Peak TOPS = 2 × Frequency × MACs / 10^3",
            "Energy Efficiency: Hexagon NPU ~15 TOPS/W vs discrete GPU ~2 TOPS/W",
            "Zero-Data Transmission: Cloud Bytes Uploaded = 0 KB"
        )).toString(),
        pageCount = 8,
        audioDurationSeconds = 0,
        isFavorite = true,
        npuProcessingTimeMs = 142,
        tokenCount = 1450
    )

    val bioMaterial = StudyMaterial(
        id = 2L,
        title = "BIO 210: Cellular Bioenergetics & ATP Synthase Mechanism",
        subject = "Biology",
        type = MaterialType.PDF,
        sourceFileName = "BIO210_Mitochondrial_Chemiosmosis.pdf",
        rawContent = """
Lecture Module: Chemiosmotic Coupling & ATP Synthase.

1. Overview of Mitochondrial Respiration:
Cellular respiration couples the oxidation of nutrient substrates to the generation of adenosine triphosphate (ATP). In the inner mitochondrial membrane, complexes I through IV transfer electrons derived from NADH and FADH2 to the terminal electron acceptor, molecular oxygen (O2), reducing it to water (H2O).

2. The Proton Motive Force (PMF):
As electrons traverse the respiratory chain complexes, protons (H+) are translocated from the matrix into the intermembrane space. This establishes both a chemical concentration gradient (ΔpH) and an electrical potential gradient (ΔΨ). Together, these constitute the Proton Motive Force:
Δp = ΔΨ - (2.303 RT/F) × ΔpH

3. Structure & Rotary Motor of ATP Synthase (Complex V):
ATP Synthase consists of two primary functional domains:
• F0 Domain: Embedded within the lipid bilayer, containing the c-ring rotor and a-subunit proton channels. Proton translocation through the half-channels induces rotary torque on the c-ring.
• F1 Domain: Soluble catalytic portion protruding into the matrix, composed of an (αβ)3 hexamer surrounding the asymmetrical γ-subunit stalk. As the γ-subunit rotates, it sequentially induces three conformational states in the β catalytic sites: Open (O), Loose (L), and Tight (T), synthesizing ATP from ADP and inorganic phosphate (Pi).

Efficiency & Yield:
Each full 360° turn of the γ-stalk yields 3 ATP molecules. Under physiological conditions, approximately 10 protons pumped per NADH yield ~2.5 ATP.
        """.trimIndent(),
        summary = "Executive Overview:\nDetailed structural and thermodynamic breakdown of mitochondrial chemiosmosis and Complex V (ATP Synthase). Demonstrates how proton motive force drives rotational catalysis to generate cellular energy.\n\nKey Insights:\n• The electron transport chain generates an electrochemical gradient across the inner membrane.\n• F0 subunit acts as a proton-driven rotary motor; F1 subunit catalyzes ATP synthesis through alternating O, L, and T states.\n• Yields approximately 2.5 ATP per NADH and 1.5 ATP per FADH2.",
        keyConceptsJson = JSONArray(listOf(
            "Proton Motive Force (PMF)",
            "Chemiosmotic Coupling",
            "F0 Subunit (Membrane Rotor)",
            "F1 Subunit (Catalytic Hexamer)",
            "Rotational Catalysis (Open/Loose/Tight)"
        )).toString(),
        keyFormulasJson = JSONArray(listOf(
            "Δp = ΔΨ - (2.303 RT/F) × ΔpH",
            "Full 360° Stalk Rotation = 3 ATP molecules",
            "NADH Yield = ~2.5 ATP, FADH2 Yield = ~1.5 ATP"
        )).toString(),
        pageCount = 6,
        audioDurationSeconds = 0,
        isFavorite = false,
        npuProcessingTimeMs = 120,
        tokenCount = 980
    )

    val audioLectureMaterial = StudyMaterial(
        id = 3L,
        title = "EE 330: Audio DSP & Acoustic Processing on Snapdragon",
        subject = "Electrical Engineering",
        type = MaterialType.AUDIO,
        sourceFileName = "EE330_Lecture_Audio_HexagonDSP.m4a",
        rawContent = """
[00:00 - 02:15] Professor Morrison:
"Welcome back everyone. Today we are exploring real-time acoustic signal processing and voice feature extraction running on ARM-based Snapdragon systems. When you record speech in a large classroom, ambient reverberation and HVAC noise distort high-frequency formants."

[02:16 - 05:40] Professor Morrison:
"To handle this locally without sending audio to the cloud, the Snapdragon audio front-end applies adaptive beamforming and spectral subtraction. The Hexagon DSP/NPU executes Fast Fourier Transforms (FFT) and Mel-frequency cepstral coefficients (MFCC) feature extraction with sub-5 millisecond algorithmic delay."

[05:41 - 09:30] Professor Morrison:
"Once features are computed, a local Whisper-class quantized speech recognition model decodes phonemes directly in device memory. Because the NPU has direct hardware access to audio buffers, the CPU remains in low-power idle states. This is how an HP Snapdragon laptop can transcribe a 3-hour seminar while barely consuming 5% of its battery."

[09:31 - 12:00] Professor Morrison:
"Key takeaways for next week's lab: review windowing functions like Hamming and Hann, and understand how the Nyquist-Shannon sampling theorem sets our minimum sampling frequency at 2 × B."
        """.trimIndent(),
        summary = "Executive Overview:\nClassroom lecture recording by Prof. Morrison covering real-time acoustic signal processing, noise reduction beamforming, and on-device speech transcription on Snapdragon architecture.\n\nKey Insights:\n• Hexagon DSP/NPU performs local spectral subtraction and MFCC feature extraction with <5ms delay.\n• Real-time ASR runs locally without CPU strain, enabling multi-hour lecture transcription on battery.\n• Nyquist-Shannon theorem requires fs ≥ 2 × Bandwidth.",
        keyConceptsJson = JSONArray(listOf(
            "Spectral Subtraction Noise Cancellation",
            "Mel-Frequency Cepstral Coefficients (MFCC)",
            "Local Whisper-Class Speech ASR",
            "Direct Hardware Audio Buffer Access",
            "Nyquist-Shannon Sampling Theorem"
        )).toString(),
        keyFormulasJson = JSONArray(listOf(
            "Nyquist Frequency: fs ≥ 2 × B",
            "Algorithmic Latency < 5 ms",
            "Battery Power Draw < 1.2W during active DSP"
        )).toString(),
        pageCount = 1,
        audioDurationSeconds = 720,
        isFavorite = true,
        npuProcessingTimeMs = 195,
        tokenCount = 820
    )

    fun getSampleFlashcards(materialId: Long): List<Flashcard> {
        return when (materialId) {
            1L -> listOf(
                Flashcard(materialId = 1L, term = "Qualcomm Hexagon NPU", definition = "Dedicated silicon engine delivering 45 TOPS of AI compute, optimized for INT4/INT8 neural models with minimal energy consumption.", contextSource = "Architecture Overview", masteryState = 2),
                Flashcard(materialId = 1L, term = "INT4 Quantization", definition = "Compresses 32-bit floating point weights into 4-bit integers, reducing memory footprint by ~75% while maintaining inference quality.", contextSource = "Optimization Section", masteryState = 1),
                Flashcard(materialId = 1L, term = "Air-Gapped Privacy", definition = "Educational files and lecture notes are processed exclusively in local RAM, preventing any third-party data tracking or cloud transmission.", contextSource = "Privacy & Security", masteryState = 2),
                Flashcard(materialId = 1L, term = "TOPS / Watt", definition = "Key metric of AI efficiency representing trillion operations per second per watt of electrical power consumed.", contextSource = "Power Efficiency", masteryState = 0)
            )
            2L -> listOf(
                Flashcard(materialId = 2L, term = "Proton Motive Force (PMF)", definition = "Electrochemical gradient generated by proton pumping across the inner mitochondrial membrane, combining electrical potential and pH gradient.", contextSource = "Chemiosmosis", masteryState = 1),
                Flashcard(materialId = 2L, term = "F0 Rotor", definition = "Membrane-bound subunit of ATP synthase that rotates as protons pass through its c-ring channels.", contextSource = "Complex V Mechanics", masteryState = 2),
                Flashcard(materialId = 2L, term = "Binding Change Mechanism", definition = "Paul Boyer's model where rotation of the γ stalk induces Open, Loose, and Tight conformational shifts in β subunits to synthesize ATP.", contextSource = "Rotary Catalysis", masteryState = 0)
            )
            else -> listOf(
                Flashcard(materialId = 3L, term = "MFCC Feature Extraction", definition = "Mel-Frequency Cepstral Coefficients represent the short-term power spectrum of sound based on human auditory perception.", contextSource = "Audio Lecture @ 03:15", masteryState = 1),
                Flashcard(materialId = 3L, term = "Nyquist-Shannon Theorem", definition = "States that to avoid aliasing, a continuous signal must be sampled at a frequency greater than twice its highest component frequency.", contextSource = "Audio Lecture @ 09:40", masteryState = 2)
            )
        }
    }

    fun getSampleQuiz(materialId: Long): List<QuizQuestion> {
        return when (materialId) {
            1L -> listOf(
                QuizQuestion(
                    materialId = 1L,
                    question = "What is the peak AI compute capability of the Snapdragon X Elite Hexagon NPU?",
                    optionsJson = JSONArray(listOf("10 TOPS", "20 TOPS", "45 TOPS", "100 TOPS")).toString(),
                    correctOptionIndex = 2,
                    explanation = "The Snapdragon X Elite Hexagon NPU delivers 45 TOPS, fulfilling and exceeding Copilot+ AI PC requirements.",
                    citation = "Section 1: Hardware Specifications"
                ),
                QuizQuestion(
                    materialId = 1L,
                    question = "Why is local INT4 quantization crucial for on-device student study assistants?",
                    optionsJson = JSONArray(listOf(
                        "It fits large language models into unified PC memory while slashing bandwidth and power consumption",
                        "It requires continuous internet connection to decrypt weights",
                        "It increases floating-point precision to 64 bits",
                        "It prevents students from modifying their notes"
                    )).toString(),
                    correctOptionIndex = 0,
                    explanation = "INT4 quantization dramatically shrinks model size, allowing high-throughput inference (>40 tokens/s) within device memory.",
                    citation = "Section 3: Quantization & Low-Latency"
                ),
                QuizQuestion(
                    materialId = 1L,
                    question = "How does on-device processing ensure student privacy?",
                    optionsJson = JSONArray(listOf(
                        "All documents and voice notes remain in local memory with 0 KB sent to cloud servers",
                        "Files are backed up to public academic archives",
                        "Cloud providers sign non-disclosure agreements automatically",
                        "Data is deleted by the remote server every hour"
                    )).toString(),
                    correctOptionIndex = 0,
                    explanation = "On-device AI executes fully air-gapped on the local Snapdragon silicon, keeping sensitive coursework 100% private.",
                    citation = "Section 2: Zero-Cloud Privacy Guarantee"
                )
            )
            2L -> listOf(
                QuizQuestion(
                    materialId = 2L,
                    question = "What constitutes the electrochemical gradient of the Proton Motive Force?",
                    optionsJson = JSONArray(listOf(
                        "Electrical membrane potential (ΔΨ) and pH concentration difference (ΔpH)",
                        "Gravitational potential and osmotic hydrostatic pressure",
                        "Thermal convection and sodium concentration",
                        "ATP hydrolysis rate in the cytosol"
                    )).toString(),
                    correctOptionIndex = 0,
                    explanation = "PMF consists of an electrical component (membrane potential) and a chemical component (pH gradient).",
                    citation = "Section 2: The Proton Motive Force"
                ),
                QuizQuestion(
                    materialId = 2L,
                    question = "How many ATP molecules are synthesized per full 360° rotation of the ATP synthase γ stalk?",
                    optionsJson = JSONArray(listOf("1 ATP", "2 ATP", "3 ATP", "12 ATP")).toString(),
                    correctOptionIndex = 2,
                    explanation = "Each full rotation drives the three catalytic β sites through their Open, Loose, and Tight cycles, generating 3 ATP.",
                    citation = "Section 3: Rotational Mechanism"
                )
            )
            else -> listOf(
                QuizQuestion(
                    materialId = 3L,
                    question = "What is the minimum sampling frequency required to digitize an audio signal of bandwidth B without aliasing?",
                    optionsJson = JSONArray(listOf("fs ≥ 0.5 × B", "fs ≥ B", "fs ≥ 2 × B", "fs ≥ 10 × B")).toString(),
                    correctOptionIndex = 2,
                    explanation = "The Nyquist-Shannon sampling theorem mandates that sampling frequency fs must be at least twice the maximum signal frequency (2 × B).",
                    citation = "Audio Lecture @ 09:40"
                )
            )
        }
    }

    fun getSampleChat(materialId: Long): List<ChatMessage> {
        return listOf(
            ChatMessage(
                materialId = materialId,
                isUser = true,
                message = "What are the biggest advantages of running this study assistant locally on my Snapdragon HP laptop instead of using a cloud tool?",
                timestamp = System.currentTimeMillis() - 60000
            ),
            ChatMessage(
                materialId = materialId,
                isUser = false,
                message = "Running locally on your Snapdragon HP PC delivers 3 decisive advantages:\n\n1. Complete Privacy & Air-Gapped Security: Your lecture recordings, private notes, and exam prep never leave your device. 0 KB uploaded to external cloud servers.\n\n2. Zero Latency & Instant Offline Access: Powered by the 45 TOPS Hexagon NPU, answers and summaries generate at >44 tokens/second without internet or server queue delays.\n\n3. All-Day Battery Efficiency: The NPU consumes <3W of power compared to heavy CPU/GPU execution, allowing you to transcribe and study through back-to-back lectures on a single charge.",
                citation = "[Qualcomm® Hexagon™ NPU Architecture • Local Quantized Engine]",
                inferenceLatencyMs = 16,
                tokensPerSec = 45.2f,
                timestamp = System.currentTimeMillis() - 55000
            )
        )
    }
}
