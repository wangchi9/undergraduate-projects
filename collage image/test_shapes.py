"""Test modules Circle, Rectangle and Line."""

import unittest
import media
from oval import Oval
from rectangle import Rectangle
from line import Line

class OvalTestCase(unittest.TestCase):
    '''Test creation and use of a Oval.'''
    
    def setUp(self):
        self.circle = Oval(40, 40, 50, 50, media.yellow, 5)
        
    def tearDown(self):
        self.rect = None
            
    def testString(self):
        assert str(self.circle) == 'Oval @ ( 40 , 40 ) height = 50, width = 50',\
               'mismatch in string version of oval'
        
    def testGetPriority(self):
        assert self.circle.get_priority() == 5, \
               'mismatch in initial priority value'

    def testSetGetPriority(self):
        self.circle.set_priority(3)
        assert self.circle.get_priority() == 3, \
               'mismatch in new priority value'

class RectangleTestCase(unittest.TestCase):
    '''Test creation and use of a Rectangle.'''
    
    def setUp(self):
        self.rect = Rectangle(0, 150, 200, 50, media.forestgreen, 1)
        
    def tearDown(self):
        self.rect = None
            
    def testString(self):
        assert str(self.rect) == 'Rectangle @ ( 0 , 150 ) height = 50, ' + \
               'width = 200', 'mismatch in string version of rectangle'
        
    def testGetPriority(self):
        assert self.rect.get_priority() == 1, \
               'mismatch in initial priority value'

    def testSetGetPriority(self):
        self.rect.set_priority(10)
        assert self.rect.get_priority() == 10, \
               'mismatch in new priority value'

class LineTestCase(unittest.TestCase):
    '''Test creation and use of a Line.'''
    
    def setUp(self):
        self.line = Line(115, 85, 125, 85, media.black, 7)
        
    def tearDown(self):
        self.line = None
            
    def testString(self):
        assert str(self.line) == 'Line @ ( 115 , 85 ) to ( 125 , 85 )',\
               'mismatch in string version of line'
        
    def testGetPriority(self):
        assert self.line.get_priority() == 7, \
               'mismatch in initial priority value'

    def testSetGetPriority(self):
        self.line.set_priority(5)
        assert self.line.get_priority() == 5, \
               'mismatch in new priority value'

def oval_suite():
    return unittest.TestLoader().loadTestsFromTestCase(OvalTestCase)

def rectangle_suite():
    return unittest.TestLoader().loadTestsFromTestCase(RectangleTestCase)

def line_suite():
    return unittest.TestLoader().loadTestsFromTestCase(LineTestCase)

runner = unittest.TextTestRunner()
runner.run(oval_suite())
runner.run(rectangle_suite())
runner.run(line_suite())