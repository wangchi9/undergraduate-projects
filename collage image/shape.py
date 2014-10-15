import media

class Shape(object):
   """The Shape stores the location of a shape to be added to the 
   scene, as well as the implied ability to be drawn onto the scene."""
   
   def __init__(self, x=0, y=0, col=media.white, priority=0):
      """Initialize the X and Y coordinates of the Shape (X and Y must be 
      positive, and default to zero if not specified). Also initialize the 
      colour of the Shape (default colour is white), and the priority of this
      Shape in the scene (default is zero).""" 

      self.x = x
      self.y = y
      self.colour = col
      self.priority = priority
      
   def draw(self):
      """The generic draw method. Shapes are general concepts and cannot be 
      drawn on an actual scene, so this method has no actual implementation."""

      pass
   
   def __str__(self):
      """Return the string representation of this Shape by indicating its
      coordinates."""

      return "Shape @ ( " + str(self.x) + " , " + str(self.y) + " )"
      
   def set_priority(self, priority=0):
      """Set the non-negative priority value for this Shape in the scene, 
      where low priority values indicate that the Shape is to be drawn first."""

      self.priority = priority
      
   def get_priority(self):
      """Return the non-negative priority value for this Shape."""

      return self.priority
   
