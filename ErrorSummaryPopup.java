import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class ErrorSummaryPopup extends Shell {

    private Table table;

    public ErrorSummaryPopup(Display display) {
        super(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        createContents();
    }

    private void createContents() {
        setText("Erreur de synthèse");
        setSize(600, 400);
        setLayout(new GridLayout(1, false));

        table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        String[] titles = {"Type de contrôle", "Message d'erreur", "Objets en erreur", "Correction automatique"};
        for (String title : titles) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(title);
        }

        // Définissez la largeur des colonnes
        table.getColumn(0).setWidth(150);
        table.getColumn(1).setWidth(200);
        table.getColumn(2).setWidth(150);
        table.getColumn(3).setWidth(150);

        // Ajoutez une méthode pour ajouter des lignes à la table
        addErrorRow("Type de contrôle 1", "Message d'erreur 1", null);
        addErrorRow("Type de contrôle 2", "Message d'erreur 2", null);
        // Ajoutez autant de lignes que nécessaire

        pack();
    }

    private void addErrorRow(String controlType, String errorMessage, ArrayList<MyClassObject> objectsInError) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(0, controlType);
        item.setText(1, errorMessage);
        // Pour les objets en erreur, vous pouvez les afficher comme vous le souhaitez
        // Par exemple, vous pouvez convertir la liste d'objets en une chaîne
        // item.setText(2, convertObjectsToString(objectsInError));

        Button autoFixButton = new Button(table, SWT.PUSH);
        autoFixButton.setText("Correction automatique");
        autoFixButton.addListener(SWT.Selection, event -> {
            // Mettez ici le code pour déclencher la correction automatique
        });
        table.getColumn(3).setControl(autoFixButton);
    }

    // Ajoutez d'autres méthodes selon vos besoins, par exemple pour convertir une liste d'objets en une chaîne

    @Override
    protected void checkSubclass() {
        // Permet à la sous-classe de Shell de ne pas lever d'exception
    }

    public static void main(String[] args) {
        Display display = new Display();
        ErrorSummaryPopup popup = new ErrorSummaryPopup(display);
        popup.open();
        while (!popup.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
