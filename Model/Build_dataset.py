import pandas as pd
from pathlib import Path

# Adjust these paths👇
ROOT_DIR = Path(r"C:/Users/Hello/Desktop/Folders/Projects/DataCombination")
OUTPUT_CSV = Path(r"C:/Users/Hello/Desktop/Folders/Projects/DataCombination/dataset.csv")
CLASS_COL = 'name'


def process_csv(csv_path, txt_path, class_col=CLASS_COL, random_state=42):
    deprecated = set(txt_path.read_text().split())
    df = pd.read_csv(csv_path)
    df_depr = df[df[class_col].isin(deprecated)].copy()
    df_non = df[~df[class_col].isin(deprecated)]
    n = len(df_depr)
    sample_non = df_non.sample(n=n, random_state=random_state) if len(df_non) >= n else df_non
    df_depr['deprecated'] = 1
    sample_non['deprecated'] = 0
    return pd.concat([df_depr, sample_non], ignore_index=True)


all_dfs = []
for proj in ROOT_DIR.iterdir():
    if not proj.is_dir(): continue
    for csv_path in proj.glob('*_buggy.csv'):
        txt_path = proj / (csv_path.stem + '_deprecated.txt')
        if not txt_path.exists(): continue
        df = process_csv(csv_path, txt_path)
        if not df.empty:
            df['project'] = proj.name
            df['bug_id'] = csv_path.stem
            all_dfs.append(df)

if all_dfs:
    pd.concat(all_dfs).to_csv(OUTPUT_CSV, index=False)
    print(f"Saved dataset with {sum(len(df) for df in all_dfs)} rows to {OUTPUT_CSV}")
else:
    print("No data processed.")
