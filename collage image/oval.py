import media
from shape import Shape

class Oval(Shape):
    '''
    Draw a Oval with the center (x,y)  and  width and heigh.
    the class has methods:
    __init__
    __str__
    draw
    '''
    
    def __init__(self, x=0, y=0, width=0, height=0, col=media.white, 
                 priority=0):
        '''
        Initialize the attributes of the Oval object. The x and y values
        represent the center of the oval, and the width and height of the oval
        are passed in as width and height. All input values are integers, 
        except for colour, which is a media.color value. 
        '''
        
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        self.col = col
        self.priority = priority
              
    def __str__(self):
        '''
        Returns the center position of this Oval, along with the height and
        width, as a string with the following formatting: 
        Oval @ ( 50 , 75 ) height = 40, width = 30
        '''
        
        str1 = "Oval @ ( " + str(self.x) + " , " + str(self.y) + " ) "
        str2 = "height = " + str(self.height) + ", width = " + str(self.width)
        
        return str1 + str2   
    
    def draw(self, pic):
        '''
        Draw this Oval on the Picture provided. Use the data attributes of this
        Oval to specify the location, height, width and colour of the oval to
        be drawn on the Picture. 
        '''
        x = int(self.x - 0.5 * self.width)
        y = int(self.y - 0.5 * self.height)
        media.add_oval_filled(pic, x, y, self.width, self.height, self.col)
        
        return pic  