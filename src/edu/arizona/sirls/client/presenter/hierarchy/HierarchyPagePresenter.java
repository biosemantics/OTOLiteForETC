package edu.arizona.sirls.client.presenter.hierarchy;

import java.util.ArrayList;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.event.hierarchy.AddStructureAsChildEvent;
import edu.arizona.sirls.client.event.hierarchy.AddStructureAsChildEventHandler;
import edu.arizona.sirls.client.event.hierarchy.DeleteNodeEvent;
import edu.arizona.sirls.client.event.hierarchy.DeleteNodeEventHandler;
import edu.arizona.sirls.client.event.hierarchy.DragStructureStartEvent;
import edu.arizona.sirls.client.event.hierarchy.DragStructureStartEventHandler;
import edu.arizona.sirls.client.event.hierarchy.DragTreeNodeStartEvent;
import edu.arizona.sirls.client.event.hierarchy.DragTreeNodeStartEventHandler;
import edu.arizona.sirls.client.event.hierarchy.DropOnToTreeNodeEvent;
import edu.arizona.sirls.client.event.hierarchy.DropOnToTreeNodeEventHandler;
import edu.arizona.sirls.client.event.hierarchy.SetCopyDragEvent;
import edu.arizona.sirls.client.event.hierarchy.SetCopyDragEventHandler;
import edu.arizona.sirls.client.presenter.MainPresenter;
import edu.arizona.sirls.client.presenter.Presenter;
import edu.arizona.sirls.client.rpc.HierarchyService;
import edu.arizona.sirls.client.rpc.HierarchyServiceAsync;
import edu.arizona.sirls.client.view.hierarchy.OtoTreeNodeView;
import edu.arizona.sirls.client.view.hierarchy.StructureTermView;
import edu.arizona.sirls.shared.beans.hierarchy.Structure;
import edu.arizona.sirls.shared.beans.hierarchy.StructureNodeData;

public class HierarchyPagePresenter implements Presenter {

	public static interface Display {
		VerticalPanel getStructureListPanel();

		VerticalPanel getTreePanel();

		Button getPrepopulateBtn();

		Tree getTree();

		TreeItem getTreeRoot();

		Button getSaveBtn();

		void setTreeRoot(TreeItem root);

		Widget asWidget();
	}

	private final Display display;
	private final HandlerManager globalEventBus;
	private HandlerManager eventBus = new HandlerManager(null);
	private HierarchyServiceAsync rpcService = GWT
			.create(HierarchyService.class);

	// helper variables for drag-n-drop
	private DragType dragType;
	private Widget dragWidget;
	private TreeItem dragTreeItem;
	private boolean isCopyDrag;

	public HierarchyPagePresenter(Display display, HandlerManager globalEventBus) {
		this.display = display;
		this.globalEventBus = globalEventBus;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
		bindEvents();
		fetchStructures();
		getRootNode();
		fetchTree();
	}

	@Override
	public void bindEvents() {
		eventBus.addHandler(DragStructureStartEvent.TYPE,
				new DragStructureStartEventHandler() {

					@Override
					public void onDragStart(DragStructureStartEvent event) {
						setDragType(DragType.STRUCTURE);
						setDragWidget(event.getStructureWidget());
					}
				});

		eventBus.addHandler(DragTreeNodeStartEvent.TYPE,
				new DragTreeNodeStartEventHandler() {

					@Override
					public void onDragStart(DragTreeNodeStartEvent event) {
						setDragType(DragType.TREENODE);
						setDragTreeItem(event.getTreeNode());
					}
				});

		eventBus.addHandler(SetCopyDragEvent.TYPE,
				new SetCopyDragEventHandler() {

					@Override
					public void onSetCopyDrag(SetCopyDragEvent event) {
						setCopyDrag(event.isCopyDrag());
					}
				});

		eventBus.addHandler(DropOnToTreeNodeEvent.TYPE,
				new DropOnToTreeNodeEventHandler() {

					@Override
					public void onDrop(DropOnToTreeNodeEvent event) {
						TreeItem targetNode = event.getTreeNode();

						if (dragType.equals(DragType.STRUCTURE)) {
							StructureTermView stv = (StructureTermView) getDragWidget();
							// create a new node and drop
							new OtoTreeNodePresenter(new OtoTreeNodeView(stv
									.getData(), true), globalEventBus, eventBus)
									.addChild(targetNode);

							// remove the source from structure list
							stv.removeFromParent();
							updatePrepopulateBtnStatus();
							targetNode.setState(true);
						} else {
							TreeItem sourceNode = getDragTreeItem();

							if (validateDrop(sourceNode, targetNode)) {
								cloneNodeToTarget(sourceNode, targetNode);
								if (!isCopyDrag) {
									sourceNode.remove();
								}
								targetNode.setState(true);
								updatePrepopulateBtnStatus();
							}
						}
					}
				});

		eventBus.addHandler(DeleteNodeEvent.TYPE, new DeleteNodeEventHandler() {

			@Override
			public void onClick(DeleteNodeEvent event) {
				OtoTreeNodeView parent = (OtoTreeNodeView) event.getNode()
						.getParentItem().getWidget();
				deleteNode(event.getNode());
				parent.getTermLbl().addStyleName("to_save");
			}
		});

		eventBus.addHandler(AddStructureAsChildEvent.TYPE,
				new AddStructureAsChildEventHandler() {

					@Override
					public void onClick(AddStructureAsChildEvent event) {
						// TODO Auto-generated method stub
						/**
						 * 1. pop up input dialog
						 * 
						 * 2. validate term (already exist in this dataset,
						 * already exist in OTO)
						 * 
						 * 3. save term to DB
						 * 
						 * 4. append new node
						 */
					}
				});

		display.getSaveBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (hasDataToSave()) {
					saveTree();
				} else {
					Window.alert("You didn't make any changes. ");
				}

			}
		});

		display.getPrepopulateBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				// prepopulate the tree and refresh the page
			}
		});
	}

	private void saveTree() {
		rpcService.saveTree(MainPresenter.uploadID, getNodeDataListToSave(),
				new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						Window.alert("Tree saved successfully. ");
						$(".to_save").removeClass("to_save");
						// Window.Location.reload();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed to save the tree. Please try again later. \n\n"
								+ caught.getMessage());
					}
				});
	}

	private void deleteNode(TreeItem node) {
		// delete children first
		if (node.getChildCount() > 0) {
			for (int i = 0; i < node.getChildCount(); i++) {
				deleteNode(node.getChild(i));
			}
		}

		// then delete node itself
		Structure nodeData = ((OtoTreeNodeView) node.getWidget()).getNodeData();
		node.remove();
		if ($(display.getTreePanel().getElement()).find(
				"[term_id='" + nodeData.getId() + "']").length() < 1) {
			new StructureTermPresenter(new StructureTermView(nodeData),
					globalEventBus, eventBus).go(display
					.getStructureListPanel());
		}
	}

	/**
	 * recursively clone the node and its children
	 * 
	 * @param sourceNode
	 * @param targetNode
	 */
	private void cloneNodeToTarget(TreeItem sourceNode, TreeItem targetNode) {
		OtoTreeNodeView sourcewidget = (OtoTreeNodeView) sourceNode.getWidget();

		TreeItem srcClone = new OtoTreeNodePresenter(new OtoTreeNodeView(
				sourcewidget.getNodeData(), true), globalEventBus, eventBus)
				.addChild(targetNode);

		if (sourceNode.getChildCount() > 0) {
			for (int i = 0; i < sourceNode.getChildCount(); i++) {
				cloneNodeToTarget(sourceNode.getChild(i), srcClone);
			}
		}
	}

	/**
	 * validate tree logic before drop: no duplicate term on one path
	 * 
	 * @param sourceNode
	 * @param targetNode
	 * @return
	 */
	private boolean validateDrop(TreeItem sourceNode, TreeItem targetNode) {
		// rule #1: source != target
		if (sourceNode == targetNode) {
			Window.alert("Source node and target node are the same. ");
			return false;
		}

		// rule #2: no duplicate children
		if (targetNode.getChildCount() > 0) {
			for (int i = 0; i < targetNode.getChildCount(); i++) {
				OtoTreeNodeView child = (OtoTreeNodeView) targetNode
						.getChild(i).getWidget();
				OtoTreeNodeView src = (OtoTreeNodeView) sourceNode.getWidget();
				if (child.getNodeData().getId() == src.getNodeData().getId()) {
					Window.alert("Structure '"
							+ src.getNodeData().getTermName()
							+ "' is already a child of target node.");
					return false;
				}
			}
		}

		// rule #3: no duplidate node in any path
		ArrayList<String> parentsOfTarget = new ArrayList<String>();
		TreeItem tmp = targetNode;
		while (!$(tmp.getWidget().getElement()).attr("term_id").equals("0")) {
			parentsOfTarget
					.add($(tmp.getWidget().getElement()).attr("term_id"));
			tmp = tmp.getParentItem();
		}

		// check if node and all its children exists in parents of target
		if (hasDuplicateNodeInParents(sourceNode, parentsOfTarget)) {
			Window.alert("Cannot move the branch because it causes duplicate nodes on a path. ");
			return false;
		}

		return true;
	}

	/**
	 * check if there is changes not saved yet
	 * 
	 * @return
	 */
	private boolean hasDataToSave() {
		if ($(".to_save").length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * update the status of prepopulate button
	 */
	private void updatePrepopulateBtnStatus() {
		if (isTreeEmpty() && !hasDataToSave()) {
			display.getPrepopulateBtn().setVisible(true);
		} else {
			display.getPrepopulateBtn().setVisible(false);
		}
	}

	/**
	 * check if the tree is empty
	 * 
	 * @return
	 */
	private boolean isTreeEmpty() {
		if (display.getTreeRoot().getChildCount() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * get top level node list to save to server
	 * 
	 * @return a list of nodes with parent term ID = 0
	 */
	private ArrayList<StructureNodeData> getNodeDataListToSave() {
		ArrayList<StructureNodeData> nodeDataList = new ArrayList<StructureNodeData>();
		TreeItem root = display.getTreeRoot();
		if (root.getChildCount() > 0) {
			for (int i = 0; i < root.getChildCount(); i++) {
				nodeDataList.add(getNodeData(root.getChild(i), "0"));
			}
		}
		return nodeDataList;
	}

	/**
	 * recursively get the node
	 * 
	 * @return
	 */
	private StructureNodeData getNodeData(TreeItem node, String parentTermID) {
		// get node
		StructureNodeData nodeData = new StructureNodeData();
		nodeData.setpID(parentTermID);
		String termID = ((OtoTreeNodeView) node.getWidget()).getNodeData()
				.getId();
		nodeData.setTermID(termID);

		// get its children
		ArrayList<StructureNodeData> children = new ArrayList<StructureNodeData>();
		if (node.getChildCount() > 0) {
			for (int i = 0; i < node.getChildCount(); i++) {
				children.add(getNodeData(node.getChild(i), termID));
			}
		}
		nodeData.setChildren(children);
		
		return nodeData;
	}

	/**
	 * recursively check if node exists in parents
	 * 
	 * @param node
	 * @param parents
	 * @return
	 */
	private boolean hasDuplicateNodeInParents(TreeItem node,
			ArrayList<String> parents) {
		Structure nodeData = ((OtoTreeNodeView) node.getWidget()).getNodeData();
		String id = nodeData.getId();
		if (parents.indexOf(id) >= 0) {
			return true;
		}

		if (node.getChildCount() > 0) {
			for (int i = 0; i < node.getChildCount(); i++) {
				TreeItem child = node.getChild(i);
				if (hasDuplicateNodeInParents(child, parents)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * populate the structures list
	 */
	private void fetchStructures() {
		rpcService.getStructureList(MainPresenter.uploadID,
				new AsyncCallback<ArrayList<Structure>>() {

					@Override
					public void onSuccess(ArrayList<Structure> result) {
						for (Structure structure : result) {
							new StructureTermPresenter(new StructureTermView(
									structure), globalEventBus, eventBus)
									.go(display.getStructureListPanel());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed in getting the terms list: \n\n"
								+ caught.getMessage());
					}
				});
	}

	private void getRootNode() {
		Structure rootData = new Structure("0", "0",
				MainPresenter.uploadInfo.getGlossaryTypeName());
		TreeItem root = new OtoTreeNodePresenter(new OtoTreeNodeView(rootData,
				false), globalEventBus, eventBus).addRoot(display.getTree());
		display.setTreeRoot(root);
	}

	/**
	 * populate the saved tree
	 */
	private void fetchTree() {
		rpcService.getNodeList(MainPresenter.uploadID,
				new AsyncCallback<ArrayList<StructureNodeData>>() {

					@Override
					public void onSuccess(ArrayList<StructureNodeData> result) {
						if (result.size() < 1) {
							display.getPrepopulateBtn().setVisible(true);
						}
						for (StructureNodeData nodeData : result) {
							constructNode(nodeData, display.getTreeRoot());
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: Failed to get tree data: \n\n"
								+ caught.getMessage());

					}
				});
	}

	/**
	 * recursively construct node
	 * 
	 * @param parent
	 */
	private void constructNode(StructureNodeData nodeData, TreeItem parent) {
		// create node
		TreeItem node = new OtoTreeNodePresenter(new OtoTreeNodeView(
				new Structure(nodeData.getTermID(), nodeData.getpID(),
						nodeData.getTermName()), false), globalEventBus,
				eventBus).addChild(parent);

		// construct its children
		if (nodeData.getChildren() != null && nodeData.getChildren().size() > 0) {
			for (StructureNodeData data : nodeData.getChildren()) {
				constructNode(data, node);
			}
		}
	}

	public DragType getDragType() {
		return dragType;
	}

	public void setDragType(DragType dragType) {
		this.dragType = dragType;
	}

	public Widget getDragWidget() {
		return dragWidget;
	}

	public void setDragWidget(Widget dragWidget) {
		this.dragWidget = dragWidget;
	}

	public TreeItem getDragTreeItem() {
		return dragTreeItem;
	}

	public void setDragTreeItem(TreeItem dragTreeItem) {
		this.dragTreeItem = dragTreeItem;
	}

	public boolean isCopyDrag() {
		return isCopyDrag;
	}

	public void setCopyDrag(boolean isCopyDrag) {
		this.isCopyDrag = isCopyDrag;
	}
}
