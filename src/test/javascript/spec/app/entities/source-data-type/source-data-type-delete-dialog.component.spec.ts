/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { SourceDataTypeDeleteDialogComponent } from 'app/entities/source-data-type/source-data-type-delete-dialog.component';
import { SourceDataTypeService } from 'app/entities/source-data-type/source-data-type.service';

describe('Component Tests', () => {
    describe('SourceDataType Management Delete Component', () => {
        let comp: SourceDataTypeDeleteDialogComponent;
        let fixture: ComponentFixture<SourceDataTypeDeleteDialogComponent>;
        let service: SourceDataTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [SourceDataTypeDeleteDialogComponent]
            })
                .overrideTemplate(SourceDataTypeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SourceDataTypeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SourceDataTypeService);
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
