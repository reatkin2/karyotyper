import Target.TargetShape;


public class TestShape {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TargetGetter test2=new TargetGetter(args[0]);
		String filename;
		filename=test2.getNewFiles(args[0]);
		if(filename!=null){
			int shapeNum=test2.findBackground(filename);
			shapeNum=test2.findChromosomes(filename, shapeNum);
			TargetShape testShape=test2.shapeList.get(0);
			test2.shapeList.get(0).shapeOut();
		}
	}

}
