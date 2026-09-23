package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.pdf.PdfParser
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SnapStudy", appName)
  }

  @Test
  fun `pdf parser extracts text and metadata from generated pdf`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    PdfParser.initialize(context)

    val doc = PDDocument()
    val page = PDPage()
    doc.addPage(page)
    doc.documentInformation.title = "Neural Computing Systems"
    doc.documentInformation.author = "Dr. Snapdragon"

    val contentStream = PDPageContentStream(doc, page)
    contentStream.beginText()
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14f)
    contentStream.newLineAtOffset(50f, 700f)
    contentStream.showText("Hexagon NPU Heterogeneous Processing Architecture")
    contentStream.endText()
    contentStream.close()

    val out = ByteArrayOutputStream()
    doc.save(out)
    doc.close()

    val result = PdfParser.parseFromStream(ByteArrayInputStream(out.toByteArray()), "Fallback Title")
    assertEquals("Neural Computing Systems", result.title)
    assertEquals("Dr. Snapdragon", result.author)
    assertEquals(1, result.pageCount)
    assertTrue("Extracted text should contain target heading", result.fullText.contains("Hexagon NPU"))
  }

  @Test
  fun `pdf parser handles invalid stream gracefully`() {
    val result = PdfParser.parseFromStream(ByteArrayInputStream(byteArrayOf(0, 1, 2, 3)), "Corrupted_File")
    assertEquals("Corrupted File", result.title)
    assertTrue("Should contain fallback summary", result.fullText.contains("Course Material"))
  }
}
