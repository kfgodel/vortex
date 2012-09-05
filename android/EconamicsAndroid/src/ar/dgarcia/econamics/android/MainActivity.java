package ar.dgarcia.econamics.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;
import ar.dgarcia.econamics.messages.ArchivoPendienteTo;

/**
 * Esta clase representa el activity principal
 * 
 * @author D. García
 */
public class MainActivity extends CustomListActivity<ArchivoPendienteTo> {

	private final List<ArchivoPendienteTo> archivos = new ArrayList<ArchivoPendienteTo>();

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		super.setUpComponents();

		ArchivoPendienteTo pendiente1 = ArchivoPendienteTo.create();
		pendiente1.setNombreDeArchivo("nombre1.doc");
		pendiente1.setTamanio(0L);
		pendiente1.setUltimaModificacion(0L);
		archivos.add(pendiente1);

		ArchivoPendienteTo pendiente2 = ArchivoPendienteTo.create();
		pendiente2.setNombreDeArchivo("Nombre 2 bastante largo para ser de n archivo solo.txt");
		pendiente2.setTamanio(1024L);
		pendiente2.setUltimaModificacion(new Date().getTime());
		archivos.add(pendiente2);
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuItems()
	 */
	public ContextMenuItem<? extends CustomableListActivity<ArchivoPendienteTo>, ArchivoPendienteTo>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	public List<ArchivoPendienteTo> getElementList() {
		return archivos;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	public RenderBlock<ArchivoPendienteTo> getElementRenderBlock() {
		return new RenderBlock<ArchivoPendienteTo>() {
			public void render(View itemView, ArchivoPendienteTo item, LayoutInflater inflater) {
				TextView nombreTxt = ViewHelper.findTextView(R.id.nombre_txt, itemView);
				nombreTxt.setText(item.getNombreDeArchivo());

				TextView sizeTxt = ViewHelper.findTextView(R.id.tamanio_txt, itemView);
				double tamanioEnMegas = (item.getTamanio() / 1024d) / 1024d;
				String tamanio = String.format("%.3fMb", tamanioEnMegas);
				sizeTxt.setText(tamanio);

				TextView fechaTxt = ViewHelper.findTextView(R.id.fecha_txt, itemView);
				String fecha = String.format("%tF", new Date(item.getUltimaModificacion()));
				fechaTxt.setText(fecha);
			}
		};
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getLayoutIdForElements()
	 */
	public int getLayoutIdForElements() {
		return R.layout.archivo_item;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.activity_main;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public Object getContextMenuHeaderTitleOrId() {
		return null;
	}
}
