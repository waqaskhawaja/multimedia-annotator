/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MaTestModule } from '../../../test.module';
import { AnalysisSessionResourceDeleteDialogComponent } from 'app/entities/analysis-session-resource/analysis-session-resource-delete-dialog.component';
import { AnalysisSessionResourceService } from 'app/entities/analysis-session-resource/analysis-session-resource.service';

describe('Component Tests', () => {
    describe('AnalysisSessionResource Management Delete Component', () => {
        let comp: AnalysisSessionResourceDeleteDialogComponent;
        let fixture: ComponentFixture<AnalysisSessionResourceDeleteDialogComponent>;
        let service: AnalysisSessionResourceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnalysisSessionResourceDeleteDialogComponent]
            })
                .overrideTemplate(AnalysisSessionResourceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnalysisSessionResourceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisSessionResourceService);
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
