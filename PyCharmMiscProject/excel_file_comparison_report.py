import pandas as pd
from openpyxl import Workbook
from openpyxl.styles import PatternFill
from openpyxl.utils.dataframe import dataframe_to_rows

def compare_excel_files(file1_path, file2_path, output_path):
    # Lire les fichiers Excel
    df1 = pd.read_excel(file1_path)
    df2 = pd.read_excel(file2_path)
    
    # Créer des copies triées pour la comparaison
    df1_sorted = df1.sort_values(by=list(df1.columns)).reset_index(drop=True)
    df2_sorted = df2.sort_values(by=list(df2.columns)).reset_index(drop=True)
    
    # Créer un nouveau workbook Excel
    wb = Workbook()
    ws = wb.active
    ws.title = "Comparaison"
    
    # Définir les couleurs de remplissage
    vert = PatternFill(start_color='90EE90', end_color='90EE90', fill_type='solid')  # Ajout
    rouge = PatternFill(start_color='FFB6C1', end_color='FFB6C1', fill_type='solid')  # Suppression
    bleu = PatternFill(start_color='ADD8E6', end_color='ADD8E6', fill_type='solid')   # Modification
    
    # Convertir les DataFrames en ensembles de tuples
    rows1 = set(map(tuple, df1_sorted.values))
    rows2 = set(map(tuple, df2_sorted.values))
    
    # Identifier les différences
    added_rows = rows2 - rows1
    deleted_rows = rows1 - rows2
    
    # Créer un DataFrame pour le rapport
    all_rows = []
    status_list = []
    
    # Ajouter l'en-tête
    headers = list(df1.columns) + ['Status']
    ws.append(headers)
    
    # Traiter toutes les lignes de df2 (nouvelles et inchangées)
    for row in df2.values:
        row_tuple = tuple(row)
        if row_tuple in added_rows:
            status = "Ajouté"
            all_rows.append(list(row))
            status_list.append(status)
    
    # Traiter les lignes supprimées de df1
    for row in df1.values:
        row_tuple = tuple(row)
        if row_tuple in deleted_rows:
            status = "Supprimé"
            all_rows.append(list(row))
            status_list.append(status)
    
    # Identifier et traiter les lignes modifiées
    for row1 in df1.values:
        for row2 in df2.values:
            if any(row1 == row2) and not all(row1 == row2):
                status = "Modifié"
                all_rows.append(list(row2))
                status_list.append(status)
                break
    
    # Ajouter les lignes inchangées
    for row in df2.values:
        row_tuple = tuple(row)
        if (row_tuple not in added_rows and 
            not any(all(r == row) for r in deleted_rows) and 
            not any(status == "Modifié" for r, status in zip(all_rows, status_list) if all(r == row))):
            status = "Inchangé"
            all_rows.append(list(row))
            status_list.append(status)
    
    # Écrire les données dans le fichier Excel avec la coloration
    for row, status in zip(all_rows, status_list):
        row_data = list(row) + [status]
        ws.append(row_data)
        row_idx = ws.max_row
        fill = None
        
        if status == "Ajouté":
            fill = vert
        elif status == "Supprimé":
            fill = rouge
        elif status == "Modifié":
            fill = bleu
            
        if fill:
            for cell in ws[row_idx]:
                cell.fill = fill
    
    # Ajuster la largeur des colonnes
    for column in ws.columns:
        max_length = 0
        column_letter = column[0].column_letter
        for cell in column:
            try:
                if len(str(cell.value)) > max_length:
                    max_length = len(str(cell.value))
            except:
                pass
        adjusted_width = (max_length + 2)
        ws.column_dimensions[column_letter].width = adjusted_width
    
    # Sauvegarder le fichier
    wb.save(output_path)
    print(f"Rapport de comparaison sauvegardé dans : {output_path}")

if __name__ == '__main__':
    # Exemple d'utilisation
    file1_path = "ancien_fichier.xlsx"
    file2_path = "nouveau_fichier.xlsx"
    output_path = "rapport_comparaison.xlsx"
    compare_excel_files(file1_path, file2_path, output_path)