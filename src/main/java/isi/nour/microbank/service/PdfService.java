package isi.nour.microbank.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import isi.nour.microbank.model.Account;
import isi.nour.microbank.model.Operation;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

public class PdfService {

    // Génère le relevé de compte au format PDF dans le flux de sortie fourni
    public void genererReleve(Account account, List<Operation> operations,
                              String debut, String fin,
                              OutputStream out) throws IOException {

        PdfWriter writer   = new PdfWriter(out);
        PdfDocument pdf    = new PdfDocument(writer);
        Document document  = new Document(pdf);

        // En-tête
        document.add(new Paragraph("MICROBANK")
                .setBold().setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("RELEVÉ DE COMPTE")
                .setBold().setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(" "));

        // Informations du compte
        document.add(new Paragraph("Client : "
                + account.getClient().getPrenom() + " "
                + account.getClient().getNom()));
        document.add(new Paragraph("Compte : " + account.getNumeroCompte()));
        document.add(new Paragraph("Type : "   + account.getType()));
        document.add(new Paragraph("Solde actuel : " + account.getSolde() + " FCFA"));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Période : " + debut + " au " + fin));

        // Tableau des opérations
        float[] columnWidths = {2f, 2f, 1.5f, 1.5f, 1.5f, 3f};
        Table table = new Table(columnWidths).useAllAvailableWidth();

        // En-têtes du tableau
        table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Référence").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Type").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Montant").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Solde après").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()));

        // Lignes + calcul des totaux
        BigDecimal totalDepots   = BigDecimal.ZERO;
        BigDecimal totalRetraits = BigDecimal.ZERO;

        for (Operation op : operations) {
            table.addCell(op.getDateOperation().toString());
            table.addCell(op.getReference());
            table.addCell(op.getType());
            table.addCell(op.getMontant() + " FCFA");
            table.addCell(op.getSoldeApres() + " FCFA");
            table.addCell(op.getDescription() != null ? op.getDescription() : "");

            if ("DEPOT".equals(op.getType())) {
                totalDepots = totalDepots.add(op.getMontant());
            } else if ("RETRAIT".equals(op.getType())) {
                totalRetraits = totalRetraits.add(op.getMontant());
            }
        }

        document.add(table);
        document.add(new Paragraph(" "));

        // Totaux finaux
        document.add(new Paragraph("Total dépôts : "   + totalDepots   + " FCFA").setBold());
        document.add(new Paragraph("Total retraits : "  + totalRetraits + " FCFA").setBold());
        document.add(new Paragraph("Solde final : "     + account.getSolde() + " FCFA").setBold());

        document.close();
    }
}