/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { DataRecordDeleteDialogComponent } from 'app/entities/data-record/data-record-delete-dialog.component';
import { DataRecordService } from 'app/entities/data-record/data-record.service';

describe('Component Tests', () => {
    describe('DataRecord Management Delete Component', () => {
        let comp: DataRecordDeleteDialogComponent;
        let fixture: ComponentFixture<DataRecordDeleteDialogComponent>;
        let service: DataRecordService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [DataRecordDeleteDialogComponent]
            })
                .overrideTemplate(DataRecordDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DataRecordDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataRecordService);
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
