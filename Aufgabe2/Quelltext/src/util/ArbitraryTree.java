package util;

public class ArbitraryTree<ContentType> {

	private ArbitraryTree<ContentType> predecessor;
	
    private class BTNode<CT> {
      
        private CT content;
        private List<ArbitraryTree<CT>> successors;

        public BTNode(CT pContent) {
            this.content = pContent;
            successors = new List<ArbitraryTree<CT>>();
        }
        
    }

    private BTNode<ContentType> node;

    public ArbitraryTree() {
        this.node = null;
    }

    public ArbitraryTree(ContentType pContent) {
        if (pContent != null) {
            this.node = new BTNode<ContentType>(pContent);
        } else {
            this.node = null;
        }
    }

    public ArbitraryTree(ContentType pContent, List<ArbitraryTree<ContentType>> successors) {
        if (pContent != null) {
            this.node = new BTNode<ContentType>(pContent);
            if (successors!=null) {
            	if(!successors.isEmpty())this.node.successors = successors;
            } else {
                this.node.successors = new List<ArbitraryTree<ContentType>>();
            }
        } else {
            this.node = null;
        }
    }

    public boolean isEmpty() {
        return this.node == null;
    }

    public void setContent(ContentType pContent) {
        if (pContent != null) {
            if (this.isEmpty()) {
                node = new BTNode<ContentType>(pContent);
                this.node.successors = new List<ArbitraryTree<ContentType>>();
            }
            this.node.content = pContent;
        }
    }

    public ContentType getContent() {
        if (this.isEmpty()) {
            return null;
        } else {
            return this.node.content;
        }
    }
    
    public ArbitraryTree<ContentType> getPredecessor() {
    	return this.predecessor;
    }

    public void setSuccessors(List<ArbitraryTree<ContentType>> successors) {
        if (!this.isEmpty() && successors != null) {
            this.node.successors = successors;
           
            successors.toFirst();
            
            while(successors.hasAccess()) {
            	successors.getContent().setPredecessor(this);
            	successors.next();
            }
            
            successors.toFirst();
        }
    }
    
    public void addSuccessor(ArbitraryTree<ContentType> treeSuccessor) {
    	 if (!this.isEmpty() && treeSuccessor != null) {
             this.node.successors.append(treeSuccessor);
             treeSuccessor.setPredecessor(this);
         }
    }
    
    public void addSuccessors(List<ArbitraryTree<ContentType>> successors) {
    	if (!this.isEmpty() && successors != null) {
    		
    		 successors.toFirst();
             
             while(successors.hasAccess()) {
             	successors.getContent().setPredecessor(this);
             	successors.next();
             }
             
             successors.toFirst();
             
             this.node.successors.concat(successors);
        }
   }
    
    public void setPredecessor(ArbitraryTree<ContentType> predecessor) {
    	this.predecessor = predecessor;
    }

    public List<ArbitraryTree<ContentType>> getSuccessors() {
        if (!this.isEmpty()) {
            return this.node.successors;
        } else {
            return null;
        }
    }
}
