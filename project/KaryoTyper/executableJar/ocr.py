#!/usr/bin/env python

# Michael Pratt <michael@pratt.im>
# Parses target letters with Tesseract

import cv2.cv as cv
import cv2
import numpy
import math
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
    #api.SetPageSegMode(tesseract.PSM_SINGLE_CHAR)

    letters = []
    out = []

    image = cv2.bitwise_not(image)
    percentset = numpy.count_nonzero(image)/float(image.size)
    # If less than 3.5% of pixels are set, throw it out
    if percentset < 0.035:
        return [("",0,0) for i in range(rotations)]

    for i in range(rotations):
        scale = 2

        rot = cv2.resize(image, (0,0), fx=scale, fy=scale, interpolation=cv2.INTER_CUBIC)
        angle = i*360/rotations

        M = cv2.getRotationMatrix2D(tuple([d/2 for d in rot.shape]), 360-angle, 1)
        rot = cv2.warpAffine(rot, M, rot.shape)
        rot = cv2.dilate(rot, cv2.getGaussianKernel(3, 0))

        letters.append([cv.fromarray(rot), angle])

    for l, angle in letters:
        new = cv.CreateImage((l.width, l.height), 8, 1)
        cv.ConvertScale(l, new)

        #print angle
        #cv.ShowImage("Rotation", new)
        #cv.WaitKey(0)

        tesseract.SetCvImage(new,api)
        text=api.GetUTF8Text()
        conf=api.MeanTextConf()
        out.append((text.strip(),conf, angle))

    return out

def print_result(letters, verbose=False):
    letter_sort = sorted(letters, key=lambda let: let[1], reverse=True);
    
    if verbose:
        for result in letter_sort:
            print "Letter: %s\tConfidence: %d\tAngle: %d" % (result[0], result[1], result[2])
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
        #image=cv.LoadImage(filename, cv.CV_LOAD_IMAGE_GRAYSCALE)
        image = cv2.imread(filename, 0)
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
