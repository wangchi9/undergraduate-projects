import Tkinter
from tkFileDialog import askopenfilename
import sys

import stylechecker

if __name__ == "__main__":
    root = Tkinter.Tk()
    py_file = askopenfilename(title="Select a Python file to Stylecheck ...", 
                              initialdir=".")
    if not py_file:
        sys.exit("No file selected")

    stylechecker.process_options(['-v', '--count', py_file])
    stylechecker.input_file(py_file)
    if stylechecker.get_statistics() == []:
        print "Congrats! No style errors were detected."
