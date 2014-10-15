from lextrie import LexTrie
from gametrie import GameTrie

lextrie = LexTrie()
lextrie.insert("a", "AAA")
lextrie.insert("bb", "BBB")
gametrie = GameTrie(lextrie._root, 2)

def set_tree_again(node, num_players, players):
    
    if node._children:
        for child in node._children.keys():
            if num_players == 1:
                set_tree_again(node._children[child], players, players)
            else:
                set_tree_again(node._children[child], num_players-1, players)
                
        if num_players == players:        # me 
            if node._data:
                node._data = None
            else:
                for child in node._children.keys():
                    if node._children[child]._data:
                        node._data = 'is winning'
        else:
            for child in node._children.keys():
                if node._children[child]._data:
                    node._data = 'is winning'
    
    else:
        if num_players == players:
            if node._data:
                node._data = None
            else:
                node._data = 'is winning'

        else:
            if node._data:
                node._data = 'is winning'
                
set_tree_again(lextrie._root, 1, 2)