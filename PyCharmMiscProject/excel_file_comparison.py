
import pandas as pd

def compare_excel_files(file1_path, file2_path):
    # Lire les fichiers Excel
    df1 = pd.read_excel(file1_path)
    df2 = pd.read_excel(file2_path)
    
    # Créer des copies triées pour la comparaison
    df1_sorted = df1.sort_values(by=list(df1.columns)).reset_index(drop=True)
    df2_sorted = df2.sort_values(by=list(df2.columns)).reset_index(drop=True)
    
    # Convertir les DataFrames en ensembles de tuples pour faciliter la comparaison
    rows1 = set(map(tuple, df1_sorted.values))
    rows2 = set(map(tuple, df2_sorted.values))
    
    # Identifier les différences
    added_rows = rows2 - rows1
    deleted_rows = rows1 - rows2
    
    # Identifier les lignes modifiées
    modified_rows = []
    for row1 in df1.values:
        for row2 in df2.values:
            # Si certaines valeurs sont identiques mais pas toutes,
            # on considère que c'est une modification
            if any(row1 == row2) and not all(row1 == row2):
                modified_rows.append({
                    'ancien': row1,
                    'nouveau': row2
                })
    
    # Afficher le résumé des changements
    print("\nRésumé des changements :")
    print("-----------------------")
    
    print("\nLignes ajoutées :")
    if added_rows:
        for row in added_rows:
            print(list(row))
    else:
        print("Aucune")
        
    print("\nLignes supprimées :")
    if deleted_rows:
        for row in deleted_rows:
            print(list(row))
    else:
        print("Aucune")
        
    print("\nLignes modifiées :")
    if modified_rows:
        for modification in modified_rows:
            print(f"Ancien : {list(modification['ancien'])}")
            print(f"Nouveau : {list(modification['nouveau'])}")
            print("---")
    else:
        print("Aucune")

if __name__ == '__main__':
    # Exemple d'utilisation
    file1_path = "ancien_fichier.xlsx"
    file2_path = "nouveau_fichier.xlsx"
    compare_excel_files(file1_path, file2_path)