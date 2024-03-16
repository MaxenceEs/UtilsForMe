import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.*;

public class ErrorSummaryPopup extends PopupDialog {
    private TableViewer tableViewer;

    public ErrorSummaryPopup(Shell parentShell) {
        super(parentShell, SWT.RESIZE, true, true, false, false, false, null, null);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new FillLayout());

        Table table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        tableViewer = new TableViewer(table);
        tableViewer.setContentProvider(new ArrayContentProvider());

        // Ajoutez les colonnes avec leurs étiquettes
        TableViewerColumn column1 = new TableViewerColumn(tableViewer, SWT.NONE);
        column1.getColumn().setText("Type de contrôle");
        column1.getColumn().setWidth(150);
        column1.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                // Récupérez le type de contrôle de l'objet
                // et renvoyez-le en tant que texte
                return ""; // Modifier pour récupérer la valeur réelle
            }
        });

        TableViewerColumn column2 = new TableViewerColumn(tableViewer, SWT.NONE);
        column2.getColumn().setText("Message d'erreur");
        column2.getColumn().setWidth(300);
        column2.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                // Récupérez le message d'erreur de l'objet
                // et renvoyez-le en tant que texte
                return ""; // Modifier pour récupérer la valeur réelle
            }

            @Override
            public Color getForeground(Object element) {
                // Vous pouvez définir la couleur du texte pour les erreurs ici
                // par exemple, si l'erreur est critique, vous pouvez la rendre rouge
                return super.getForeground(element);
            }
        });

        TableViewerColumn column3 = new TableViewerColumn(tableViewer, SWT.NONE);
        column3.getColumn().setText("Correction");
        column3.getColumn().setWidth(100);
        column3.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return "Correction";
            }
        });

        // Créez l'EditingSupport pour la colonne de correction
        column3.setEditingSupport(new EditingSupport(tableViewer) {
            @Override
            protected CellEditor getCellEditor(Object element) {
                return new TextCellEditor(tableViewer.getTable());
            }

            @Override
            protected boolean canEdit(Object element) {
                // Retourne true si l'élément peut être édité
                return true;
            }

            @Override
            protected Object getValue(Object element) {
                // Retournez la valeur à afficher dans la cellule
                return "Correction";
            }

            @Override
            protected void setValue(Object element, Object value) {
                // Implémentez la logique pour la correction automatique ici
                // Par exemple, vous pouvez appeler la méthode autoCorrect ici
                // en passant l'objet à corriger
                // assurez-vous de rafraîchir la table après la correction
            }
        });

        return composite;
    }

    // Méthode pour ajouter une erreur à la table
    public void addError(String controlType, String errorMessage, ArrayList<MyClassObject> errorObjects) {
        // Créez un objet pour représenter l'erreur et ajoutez-le à la table
        // en utilisant le viewer
    }

    // Méthode pour déclencher la correction automatique
    private void autoCorrect(ArrayList<MyClassObject> objectsToCorrect) {
        // Implémentez la logique pour la correction automatique ici
    }
}
