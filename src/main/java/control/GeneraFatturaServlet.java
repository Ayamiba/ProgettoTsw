package control;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import model.ordine.OrdineBean;
import model.ordine.OrdineDAO;
import model.utente.UtenteBean;
import model.prodotto.ProdottoDAO;
import model.prodotto.ProdottoBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.io.File;

@WebServlet("/GeneraFatturaServlet")
public class GeneraFatturaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");

        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        String idOrdineStr = request.getParameter("id");
        if (idOrdineStr == null || idOrdineStr.trim().isEmpty()) {
            response.sendRedirect("ProfiloServlet?errore=id_mancante");
            return;
        }

        try {
            int idOrdine = Integer.parseInt(idOrdineStr);
            
            OrdineDAO ordineDAO = new OrdineDAO();
            OrdineBean ordine = ordineDAO.doRetrieveByKey(idOrdine);
            
            if (ordine == null) {
                response.sendRedirect("ProfiloServlet?errore=ordine_non_trovato");
                return;
            }

            ProdottoDAO prodottoDAO = new ProdottoDAO();
            List<ProdottoBean> prodotti = prodottoDAO.doRetrieveProdottiByOrdine(idOrdine);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"Fattura_Saendwave_" + idOrdine + ".pdf\"");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            
            BaseColor brandColor = new BaseColor(65, 52, 231); // #4134E7

            // --- LOGO ---
            try {
                String logoPath = getServletContext().getRealPath("/img/placeholder.png");
                if (logoPath != null) {
                    File f = new File(logoPath);
                    if(f.exists()) {
                        Image logo = Image.getInstance(logoPath);
                        logo.scaleToFit(200, 200);
                        logo.setAlignment(Element.ALIGN_RIGHT);
                        document.add(logo);
                    }
                }
            } catch (Exception e) {
                System.out.println("Logo non trovato per PDF: " + e.getMessage());
            }

            Paragraph title = new Paragraph("SÆNDWAVE - RICEVUTA D'ACQUISTO", titleFont);
            title.setAlignment(Element.ALIGN_LEFT);
            title.setSpacingAfter(20);
            document.add(title);

            // --- DATI CLIENTE E ORDINE ---
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);

            PdfPCell clientCell = new PdfPCell();
            clientCell.setBorder(PdfPCell.NO_BORDER);
            clientCell.addElement(new Paragraph("Fatturato a:", headerFont));
            clientCell.addElement(new Paragraph(utente.getNome() + " " + utente.getCognome(), normalFont));
            clientCell.addElement(new Paragraph(utente.getEmail(), normalFont));
            infoTable.addCell(clientCell);

            PdfPCell orderCell = new PdfPCell();
            orderCell.setBorder(PdfPCell.NO_BORDER);
            orderCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            Paragraph p1 = new Paragraph("Ordine Numero: #" + ordine.getIdOrdine(), headerFont);
            p1.setAlignment(Element.ALIGN_RIGHT);
            Paragraph p2 = new Paragraph("Data: " + ordine.getDataOrdine().toString(), normalFont);
            p2.setAlignment(Element.ALIGN_RIGHT);
            Paragraph p3 = new Paragraph("Stato: " + ordine.getStato(), normalFont);
            p3.setAlignment(Element.ALIGN_RIGHT);
            
            orderCell.addElement(p1);
            orderCell.addElement(p2);
            orderCell.addElement(p3);
            infoTable.addCell(orderCell);

            document.add(infoTable);

            LineSeparator ls = new LineSeparator();
            ls.setLineColor(BaseColor.LIGHT_GRAY);
            document.add(new Chunk(ls));
            document.add(new Paragraph(" ")); 

            // --- TABELLA PRODOTTI (5 Colonne: Nome, Qta, Netto, Iva, Lordo) ---
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 0.6f, 1.2f, 1f, 1.2f});
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            PdfPCell h1 = new PdfPCell(new Phrase("PRODOTTO AUDIO", tableHeaderFont));
            h1.setBackgroundColor(brandColor); h1.setPadding(8); table.addCell(h1);

            PdfPCell h2 = new PdfPCell(new Phrase("Q.TÀ", tableHeaderFont));
            h2.setBackgroundColor(brandColor); h2.setHorizontalAlignment(Element.ALIGN_CENTER); h2.setPadding(8); table.addCell(h2);

            PdfPCell h3 = new PdfPCell(new Phrase("NETTO", tableHeaderFont));
            h3.setBackgroundColor(brandColor); h3.setHorizontalAlignment(Element.ALIGN_RIGHT); h3.setPadding(8); table.addCell(h3);
            
            PdfPCell h4 = new PdfPCell(new Phrase("IVA 22%", tableHeaderFont));
            h4.setBackgroundColor(brandColor); h4.setHorizontalAlignment(Element.ALIGN_RIGHT); h4.setPadding(8); table.addCell(h4);

            PdfPCell h5 = new PdfPCell(new Phrase("LORDO", tableHeaderFont));
            h5.setBackgroundColor(brandColor); h5.setHorizontalAlignment(Element.ALIGN_RIGHT); h5.setPadding(8); table.addCell(h5);

            double sommaImponibile = 0;
            double sommaIva = 0;

            for (ProdottoBean p : prodotti) {
                // Calcoli riga singola
                double lordo = p.getPrezzo();
                double netto = lordo / 1.22;
                double iva = lordo - netto;
                
                sommaImponibile += netto;
                sommaIva += iva;

                PdfPCell c1 = new PdfPCell(new Phrase(p.getNome(), normalFont));
                c1.setPadding(8); c1.setBorderColor(BaseColor.LIGHT_GRAY); table.addCell(c1);

                PdfPCell c2 = new PdfPCell(new Phrase("1", normalFont));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER); c2.setPadding(8); c2.setBorderColor(BaseColor.LIGHT_GRAY); table.addCell(c2);

                PdfPCell c3 = new PdfPCell(new Phrase(String.format("€ %.2f", netto), normalFont));
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT); c3.setPadding(8); c3.setBorderColor(BaseColor.LIGHT_GRAY); table.addCell(c3);
                
                PdfPCell c4 = new PdfPCell(new Phrase(String.format("€ %.2f", iva), normalFont));
                c4.setHorizontalAlignment(Element.ALIGN_RIGHT); c4.setPadding(8); c4.setBorderColor(BaseColor.LIGHT_GRAY); table.addCell(c4);

                PdfPCell c5 = new PdfPCell(new Phrase(String.format("€ %.2f", lordo), normalFont));
                c5.setHorizontalAlignment(Element.ALIGN_RIGHT); c5.setPadding(8); c5.setBorderColor(BaseColor.LIGHT_GRAY); table.addCell(c5);
            }

            document.add(table);

            // --- RIEPILOGO TOTALI (Imponibile, Iva, Lordo) ---
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(45);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.setWidths(new float[]{1f, 1f});
            
            PdfPCell t1 = new PdfPCell(new Phrase("Totale Imponibile:", headerFont));
            t1.setBorder(PdfPCell.NO_BORDER); t1.setHorizontalAlignment(Element.ALIGN_RIGHT); t1.setPaddingBottom(5); totalTable.addCell(t1);
            
            PdfPCell t2 = new PdfPCell(new Phrase(String.format("€ %.2f", sommaImponibile), normalFont));
            t2.setBorder(PdfPCell.NO_BORDER); t2.setHorizontalAlignment(Element.ALIGN_RIGHT); t2.setPaddingBottom(5); totalTable.addCell(t2);
            
            PdfPCell t3 = new PdfPCell(new Phrase("Totale IVA (22%):", headerFont));
            t3.setBorder(PdfPCell.NO_BORDER); t3.setHorizontalAlignment(Element.ALIGN_RIGHT); t3.setPaddingBottom(5); totalTable.addCell(t3);
            
            PdfPCell t4 = new PdfPCell(new Phrase(String.format("€ %.2f", sommaIva), normalFont));
            t4.setBorder(PdfPCell.NO_BORDER); t4.setHorizontalAlignment(Element.ALIGN_RIGHT); t4.setPaddingBottom(5); totalTable.addCell(t4);
            
            PdfPCell t5 = new PdfPCell(new Phrase("TOTALE PAGATO:", totalFont));
            t5.setBorder(PdfPCell.NO_BORDER); t5.setHorizontalAlignment(Element.ALIGN_RIGHT); t5.setPaddingTop(10); totalTable.addCell(t5);
            
            PdfPCell t6 = new PdfPCell(new Phrase(String.format("€ %.2f", ordine.getTotale()), totalFont));
            t6.setBorder(PdfPCell.NO_BORDER); t6.setHorizontalAlignment(Element.ALIGN_RIGHT); t6.setPaddingTop(10); totalTable.addCell(t6);

            document.add(totalTable);
            
            document.add(new Paragraph(" "));
            
            // --- NOTE CLIENTE ---
            if(ordine.getDescrizione() != null && !ordine.getDescrizione().isEmpty() && !ordine.getDescrizione().equals("Nessuna istruzione particolare.")) {
                 Paragraph noteHeader = new Paragraph("Note Cliente:", headerFont);
                 noteHeader.setSpacingBefore(20);
                 document.add(noteHeader);
                 
                 Paragraph noteContent = new Paragraph(ordine.getDescrizione(), normalFont);
                 document.add(noteContent);
            }

            // --- FOOTER ---
            document.add(new Paragraph(" "));
            document.add(new Chunk(ls));
            Paragraph footer = new Paragraph("Grazie per aver scelto i professionisti di Sændwave.\nPer assistenza: info@saendwave.com", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);

            document.close();

        } catch (NumberFormatException | DocumentException e) {
            e.printStackTrace();
            response.sendRedirect("ProfiloServlet?errore=generazione_pdf_fallita");
        } catch (Exception e) {
             e.printStackTrace();
             response.sendRedirect("ProfiloServlet?errore=imprevisto_pdf");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}