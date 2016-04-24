# -*- coding: utf-8 -*-  
'''
Created on 2015/04/23

@author: pake
'''

import matplotlib.pyplot as plt
from copy import deepcopy
from cProfile import label
import numpy as np
from matplotlib.lines import Line2D


def ReadDataFile(filename):
    fp = open(filename, 'r')              
    DataList = []
    try:
        for line in fp.readlines():
            DataList.append(line.strip().split(" "))
    finally:
        fp.close()
    return DataList


def isdomination(x1,y1,x2,y2):
    if (float(x1)<float(x2) and float(y1)<float(y2)):
            return True;
    return False;

def isdominationbylist(xlist, ylist, x1, y1):
    for i in range(len(xlist)):
        if (isdomination(xlist[i], ylist[i], x1, y1)):
            print 'zipei' , xlist[i], ylist[i], x1, y1
            return True;
    return False
    
def ShowPic(Xlist, Ylist):
    #plt.xlim(0, 10)
    #plt.ylim(0, 10)
    print Xlist
    print Ylist
    
    newxlist = deepcopy(Xlist);
    newylist = deepcopy(Ylist);
    
    for i in range(len(Xlist)-1, 0, -1):
        if isdominationbylist(newxlist, newylist, Xlist[i], Ylist[i]):
            print i
            print "del", newxlist[i], newylist[i]
            del newxlist[i]
            del newylist[i]
    
    if isdominationbylist(newxlist, newylist, Xlist[0], Ylist[0]):
            print i
            print "del", newxlist[0], newylist[0]
            del newxlist[0]
            del newylist[0]
    style = styles[9]
    plt.plot(newxlist, newylist, 'o', label='MOEA/D-L', marker=style, color='black', markersize=10)
    
    #plt.plot(Xlist, Ylist, 'o', label='MOEAD-H')
    
    print len(newxlist), len(Xlist)
    #plt.show()
if __name__ == '__main__':
    #Datalist = ReadDataFile('..\data/fun_nsgaii_a0')
    Datalist1 = ReadDataFile('FUN')
    Datalist2 = ReadDataFile('FUN1')
    #Datalist3 = ReadDataFile('..\data/uav_moeadl1')
    markers = []
    for m in Line2D.markers:
        try:
            if len(m) == 1 and m != ' ':
                markers.append(m)
        except TypeError:
            pass
    styles = markers + [r'$\lambda$',r'$\bowtie$',r'$\circlearrowleft$', r'$\clubsuit$', r'$\checkmark$']
    xlist = [[],[],[]]
    ylist = [[],[],[]]
    for line in Datalist1:
        xlist[0].append(line[0])
        ylist[0].append(line[1])
    for line1 in Datalist2:
        xlist[1].append(line1[0])
        ylist[1].append(line1[1])
    #for line2 in Datalist3:
    #    xlist[2].append(line2[0])
    #    ylist[2].append(line2[1])
    plt.grid(True)    
    #plt.title('GAPA3', fontsize=22)
    plt.xlabel(r"$F_c(x)$", fontsize=20)
    plt.ylabel(r'$F_b(x)$' , fontsize=20)
    #plot1 = plt.plot(xlist[0], ylist[0], '--', label='MOEAD')
    style = styles[5]
    plot1 = plt.plot(xlist[0], ylist[0],  label='FUN1', linestyle='None', marker=style, color='black', markersize=5)
    style = styles[1]
    plot2 = plt.plot(xlist[1], ylist[1], 'o', label='FUN2', linestyle='None', marker=style, color='red', markersize=5)
    
    #plot2 = plt.plot(xlist[1], ylist[1], 'o', label='NSGAII', linestyle='None', marker=style, color='b', markersize=8)
    #plot3 = plt.plot(xlist[2], ylist[2], 'o', color='black',label='MOEAD-H')
    #ShowPic(xlist[2], ylist[2]);
    plt.legend( loc = 'upper left', shadow=True)
    plt.savefig('simple plot.png',dpi = 200)
    plt.show()
    
