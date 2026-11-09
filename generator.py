import sys
from typing import Generator, Any

def data_streamer(source_path: str) -> Generator[str, None, None]:
    """Efficiently streams data line-by-line without loading the file into RAM."""
    try:
        with open(source_path, 'r', encoding='utf-8') as file:
            for line in file:
                cleaned_line = line.strip()
                if cleaned_line:  # Skip empty lines
                    yield cleaned_line
    except FileNotFoundError:
        print(f"Error: The file {source_path} was not found.", file=sys.stderr)
    except IOError as e:
        print(f"I/O Error handling file: {e}", file=sys.stderr)

def process_item(item: str) -> Any:
    """Transformation logic for a single data unit."""
    # Place your heavy processing, cleaning, or parsing logic here
    return item.upper() 

def main() -> None:
    # 1. Configuration
    input_file = "massive_dataset.txt"
    
    # 2. Initialize the pipeline (lazy evaluation - nothing happens yet)
    stream = data_streamer(input_file)
    
    # 3. Consume the stream efficiently
    for raw_item in stream:
        processed_data = process_item(raw_item)
        print(processed_data)  # Or stream it directly into a database/output file

if __name__ == "__main__":
    main()
   #Most efficient type of code
