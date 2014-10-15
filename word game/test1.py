from trie import Trie


    
trie = Trie()
trie.insert("coo", "Coo's Data")
trie.insert("cool", "Cool's Data")
trie.insert("coo", "Coo's Data")
trie.insert("bar", "Bar's Data")
trie.insert("cooler", "Cooler's Data")
trie.insert("baristas", "Baristas's Data")
trie.insert("cook", "Cook's Data")
trie.insert("banner", "Banner's Data")
trie.insert("bag", "Bag's Data")
trie.insert("bank", "Bank's Data")

def _find_all(root, prefix):
    '''Return a list of valid words with prefix 'prefix' in this
        Trie.

        -- prefix: a string; all valid words from this Trie that begin
        with 'prefix' are returned.'''
    if len(prefix) == 0:
        return _find_all2(root)
    else:
        return _find_all(root._children[prefix[0]], prefix[1:])
    
    
def _find_all2(root, preword=''):

    words = []
    if root._children:
        for child in root._children.keys():
            words += _find_all2(root._children[child], preword+child)
        if root._data:
            words.append(preword)
    else:
        return [preword]
    return words
    
    
# print _find_all(trie._root, 'c')
players = 2 # players = number_players
def set_tree_again(node, num_players):
    
    if node._children:
        for child in node._children.keys():
            if num_players == 1:
                set_tree_again(node._children[child], players)
            else:
                set_tree_again(node._children[child], num_players-1)
                
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
 
def is_winning(node):
    return node.data()

set_tree_again(trie._root, 1)

allWinStrings = ["", "cool", "coole", "cooler", "cook",
                         "b", "ba", "ban", "bank", "bann", "banne", "banner",
                         "bari", "baris", "barist", "barista", "baristas"]
errors = [prefx for prefx in allWinStrings if not trie.find_node(prefx)._data]
print errors
