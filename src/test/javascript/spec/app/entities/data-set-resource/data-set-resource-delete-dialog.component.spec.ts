import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MaTestModule } from '../../../test.module';
import { DataSetResourceDeleteDialogComponent } from 'app/entities/data-set-resource/data-set-resource-delete-dialog.component';
import { DataSetResourceService } from 'app/entities/data-set-resource/data-set-resource.service';

describe('Component Tests', () => {
    describe('DataSetResource Management Delete Component', () => {
        let comp: DataSetResourceDeleteDialogComponent;
        let fixture: ComponentFixture<DataSetResourceDeleteDialogComponent>;
        let service: DataSetResourceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [DataSetResourceDeleteDialogComponent]
            })
                .overrideTemplate(DataSetResourceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DataSetResourceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataSetResourceService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
