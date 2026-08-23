package isi.nour.microbank.service;

import isi.nour.microbank.model.Operation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class CsvService {

    // Génère l'export CSV des opérations dans le flux de sortie fourni
    public void exporterOperations(List<Operation> operations, OutputStream out)
            throws IOException {

        PrintWriter writer = new PrintWriter(out);

        // En-tête du fichier CSV
        writer.println("Date;Reference;Type;Montant;Solde apres;Description");

        // Une ligne par opération
        for (Operation op : operations) {
            writer.println(
                    op.getDateOperation() + ";" +
                            op.getReference()     + ";" +
                            op.getType()          + ";" +
                            op.getMontant()       + ";" +
                            op.getSoldeApres()    + ";" +
                            (op.getDescription() != null ? op.getDescription() : "")
            );
        }

        writer.flush();
    }
}