package edu.arizona.sirls.client.view.hierarchy;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.arizona.sirls.client.presenter.hierarchy.HierarchyPagePresenter;

public class HierarchyPageView extends Composite implements
		HierarchyPagePresenter.Display {

	private VerticalPanel leftListPanel;
	private VerticalPanel treePanel;
	private Tree tree;
	private TreeItem root;
	private Button prepopulateBtn;
	private Button saveBtn;

	public HierarchyPageView() {
		FlexTable layout = new FlexTable();
		layout.setSize("100%", "100%");
		initWidget(layout);

		// titles
		layout.setText(0, 0, "Structures: ");
		layout.getCellFormatter().setWidth(0, 0, "20%");
		layout.getRowFormatter().addStyleName(0, "HIERARCHY_title");
		layout.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		FlexTable rightTitleTbl = new FlexTable();
		rightTitleTbl.setWidth("100%");
		layout.setWidget(0, 1, rightTitleTbl);
		layout.getCellFormatter().setWidth(0, 1, "80%");
		layout.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		rightTitleTbl.setText(0, 0, "Hierarchy: ");
		rightTitleTbl.getRowFormatter().addStyleName(0, "HIERARCHY_title");
		prepopulateBtn = new Button("Prepopulate Tree");
		saveBtn = new Button("Save Tree");
		HorizontalPanel btns = new HorizontalPanel();
		btns.setSpacing(1);
		btns.add(prepopulateBtn);
		btns.add(saveBtn);
		rightTitleTbl.setWidget(0, 1, btns);
		rightTitleTbl.getColumnFormatter().setWidth(0, "50%");
		rightTitleTbl.getColumnFormatter().setWidth(1, "50%");
		rightTitleTbl.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		prepopulateBtn.setVisible(false); // default not visible

		// left list panel
		leftListPanel = new VerticalPanel();
		leftListPanel.setSize("100%", "100%");
		leftListPanel.setSpacing(5);
		layout.getCellFormatter().addStyleName(1, 0,
				"HIERARCHY_left_structure_list");
		layout.setWidget(1, 0, leftListPanel);
		layout.getCellFormatter().setWidth(1, 0, "20%");
		layout.getCellFormatter().setHeight(1, 0, "100%");
		layout.getFlexCellFormatter().setAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		// right tree panel
		treePanel = new VerticalPanel();
		layout.setWidget(1, 1, treePanel);
		treePanel.setSize("100%", "100%");
		layout.getCellFormatter().setWidth(1, 1, "80%");
		layout.getCellFormatter().setHeight(1, 1, "100%");

		layout.getFlexCellFormatter().setAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		tree = new Tree();
		treePanel.add(tree);

	}

	@Override
	public VerticalPanel getStructureListPanel() {
		return leftListPanel;
	}

	@Override
	public VerticalPanel getTreePanel() {
		return treePanel;
	}

	@Override
	public Button getPrepopulateBtn() {
		return prepopulateBtn;
	}

	@Override
	public TreeItem getTreeRoot() {
		return root;
	}

	@Override
	public Tree getTree() {
		return tree;
	}

	@Override
	public void setTreeRoot(TreeItem root) {
		this.root = root;
		tree.addItem(root);

	}

	@Override
	public Button getSaveBtn() {
		return saveBtn;
	}

}
