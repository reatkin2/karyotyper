#!/usr/bin/env python

# Michael Pratt <michael@pratt.im>
# Parses target letters with Tesseract

import cv2.cv as cv
import tesseract
import sys
import os
import argparse

# Default rotations value
default_rotations = 16

def find_letters(image, rotations):
    """Finds letters in OpenCV image object containing ROTATION rotations of a letter."""
    api = tesseract.TessBaseAPI()
    api.Init(".","eng",tesseract.OEM_DEFAULT)
    #api.SetVariable("tessedit_char_whitelist", "0123456789ABCDEFGHUJKLMNOPQRSTUVWXYZ")
    api.SetVariable("tessedit_char_whitelist", "ABCDEFGHUJKLMNOPQRSTUVWXYZ")

    letters = []
    out = []

    for i in range(rotations):
        crop = cv.CreateImage((image.width/rotations, image.height), 8, 1)
        region = cv.GetSubRect(image, (i*image.width/rotations,0,image.width/rotations,image.height))
        cv.Copy(region, crop)
        letters.append(crop)

    for index, l in enumerate(letters):
        tesseract.SetCvImage(l,api)
        text=api.GetUTF8Text()
        conf=api.MeanTextConf()
        out.append((text.strip(),conf, index))

    return out

def print_result(letters, verbose=False):
    letter_sort = sorted(letters, key=lambda let: let[1], reverse=True);
    
    if verbose:
        for result in letter_sort:
            print "Letter: %s\tConfidence: %d\tLocation: %d" % (result[0], result[1], result[2])
    else:
        print "%s:%s:%s" % (letter_sort[0][0], letter_sort[0][1], letter_sort[0][2])

def parse_folder(folder, rotations):
    for filename in os.listdir(folder):
        try:
            image=cv.LoadImage(folder+filename, cv.CV_LOAD_IMAGE_GRAYSCALE)
        except IOError:
            continue

        output = find_letters(image, rotations)
        output_sort = sorted(output, key=lambda let: let[1], reverse=True)
        line = "%s:\t%s\t%d%%" % (filename, output_sort[0][0], output_sort[0][1])
        print line

def parse_file(filename, rotations, verbose=False):
    try:
        image=cv.LoadImage(filename, cv.CV_LOAD_IMAGE_GRAYSCALE)
    except IOError:
        print "Bad file: %s" % filename
        exit(1)

    output = find_letters(image, rotations)
    print_result(output, verbose)

if __name__=="__main__":
    parser = argparse.ArgumentParser(description='Uses Tesseract OCR to analyze the letters in an image of rotated letters.',
                                     epilog='I have no phone number error messages, so I will add this for good measure: Contact Michael Pratt for help - <michael@pratt.im> - 919-271-4250')

    parser.add_argument('rotations', metavar='ROTATIONS', type=int, nargs='?', default=default_rotations, help='Number of letter rotations in images.  Defaults to %d.' % default_rotations)
    parser.add_argument('filename', metavar='FILE', help='File or folder to be parsed, depending on options.')

    exclusive = parser.add_mutually_exclusive_group(required=False)
    exclusive.add_argument('-f', '--folder', action='store_true', help='Parse an entire folder of images.  FILE should be a folder containing images. Invalid files will be ignored.')
    exclusive.add_argument('-v', '--verbose', action='store_true', help='Print output for each letter parsed, instead of just most confident.')

    args = parser.parse_args()

    if args.folder:
        parse_folder(args.filename, args.rotations)
    else:
        parse_file(args.filename, args.rotations, args.verbose)
