import media
from shape import Shape

class Rectangle(Shape):
    '''
    Draw a rectangle with the top left corner (x,y) and width and height. 
    the class has methods:
    __init__
    __str__
    draw
    '''
    
    def __init__(self, x=0, y=0, width=0, height=0, col=media.white, 
                 priority=0):
        '''
        Initialize the attributes of the Rectangle object. The x and y values
        represent the top left corner of the rectangle, and the width 
        and height of the rectangle are passed in as width and height. 
        All input  values areintegers, except for colour, which is a media.
        color value.
        '''
        
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        self.col = col
        self.priority = priority
        
    def __str__(self):
        '''
        Returns the top left corner position of this Rectangle, along with the
        height and width, as a string with the following formatting: 
        Rectangle @ ( 50 , 75 ) height = 40, width = 30
        '''
        
        str1 = "Rectangle @ ( " + str(self.x) + " , " + str(self.y) + " ) "
        str2 = "height = " + str(self.height) + ", width = " + str(self.width)
        
        return str1 + str2
    
    def draw(self, pic):
        '''
        Draw this Rectangle on the Picture provided. Use the data attributes 
        of this Rectangle to specify the location, height, width and colour 
        of the resulting rectangle on the image. 
        '''
        
        media.add_rect_filled(pic, self.x, self.y, self.width, self.height, 
                              self.col)
        
        return pic  